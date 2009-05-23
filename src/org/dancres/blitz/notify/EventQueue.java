package org.dancres.blitz.notify;

import java.io.IOException;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;

import net.jini.config.ConfigurationException;
import net.jini.core.event.RemoteEventListener;

import org.dancres.blitz.ActiveObject;
import org.dancres.blitz.ActiveObjectRegistry;
import org.dancres.blitz.Logging;
import org.dancres.blitz.config.ConfigurationFactory;
import org.dancres.blitz.mangler.MangledEntry;
import org.dancres.blitz.oid.OID;
import org.dancres.blitz.stats.StatsBoard;
import org.dancres.blitz.txn.TxnState;
import org.dancres.blitz.txn.TxnManager;
import EDU.oswego.cs.dl.util.concurrent.BoundedLinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import org.dancres.blitz.util.QueueStatGenerator;

/**
   The heart of the notify implementation.  Events are passed to here and
   processed against templates.  Resulting events are then passed into the
   notification pool for dispatch. <P>

   <p>For a transaction end event, we remove any associated registrations held
   in EventGeneratorFactory.</p>
 */
public class EventQueue implements ActiveObject {
    static Logger theLogger =
        Logging.newLogger("org.dancres.blitz.notify.EventQueue");

    private static final EventQueue theEventQueue = new EventQueue();

    public static EventQueue get() {
        return theEventQueue;
    }

    private PooledExecutor theProcessors;

    private long theEventCount = 0;

    private RemoteEventDispatcher theDispatcher = new RemoteEventDispatcher();

    private int NUM_PROCESSORS = 10;

    private int QUEUE_BOUND = 100;

    private EventQueue() {
        ActiveObjectRegistry.add(this);

        try {
            NUM_PROCESSORS = ((Integer)
                  ConfigurationFactory.getEntry("maxEventProcessors",
                                                int.class,
                                                new Integer(1))).intValue();
            QUEUE_BOUND = ((Integer)
                  ConfigurationFactory.getEntry("eventQueueBound",
                                                int.class,
                                                new Integer(0))).intValue();

            theLogger.log(Level.INFO, "Max Event Processors: " +
                          NUM_PROCESSORS);
            theLogger.log(Level.INFO, "Event Queue Bound: " + QUEUE_BOUND);

        } catch (ConfigurationException aCE) {
            theLogger.log(Level.SEVERE, "Failed to load config", aCE);
        }
    }

    public void add(QueueEvent anEvent) {
        add(anEvent, false);
    }

    public void add(QueueEvent anEvent, boolean aWaitIndicator) {
        if (TxnManager.get().isRecovery())
            return;

        // No-one listening, no point in doing work
        //
        if (EventGeneratorFactory.get().getCount() == 0)
            return;

        if (theLogger.isLoggable(Level.FINEST))
            theLogger.log(Level.FINEST, "Event: " + anEvent.getType() + ", " +
                          anEvent.getTxn() + ", " + anEvent.getContext());
        try {
            /*
             * WTF?  For some reason, javac can't resolve the two types below to
             * DispatchTask without explicit casting - suspecting cursed generics
             * and type reasoning grrrrr
             */
            DispatchTask aTask = (aWaitIndicator == true) ?
                    (DispatchTask) new BlockingDispatchImpl(this, anEvent) :
                    (DispatchTask) new NonblockingDispatchImpl(this, anEvent);

            theProcessors.execute(aTask);

            aTask.block();
        } catch (InterruptedException anIE) {
            theLogger.log(Level.FINEST, "Failed to queue event");
        }
    }

    public void insert(EventGenerator anEventGenerator) throws IOException {
		EventGeneratorFactory.get().addTemporary(anEventGenerator);
	}
	
    /**
       @todo Lock txn during adding of reg for non-null txns.
    */
    public void register(MangledEntry aTemplate, TxnState aTxn,
                         RemoteEventListener aListener, long aLeaseTime,
                         MarshalledObject aHandback,
                         Registrar aRegistrar)
        throws IOException, RemoteException {


        if (aTxn == null) {
            EventGenerator myGenerator =
                EventGeneratorFactory.get().newPersistentGenerator(aTemplate,
                                                                   aListener,
                                                                   aLeaseTime,
                                                                   aHandback);

            long myFirstSeqNum = myGenerator.getStartSeqNum();

            aRegistrar.newRegistration(myGenerator.getSourceId(),
                                       myFirstSeqNum,
                                       new SpaceNotifyUID(myGenerator.getId()));
        } else {
            EventGenerator myGenerator =
                EventGeneratorFactory.get().newTransientGenerator(aTemplate,
                                                                  aListener,
                                                                  aLeaseTime,
                                                                  aHandback,
                                                                  aTxn);

            long myFirstSeqNum = myGenerator.getStartSeqNum();

            aRegistrar.newRegistration(myGenerator.getSourceId(),
                                       myFirstSeqNum,
                                       new SpaceNotifyUID(myGenerator.getId()));
        }
    }

    public void registerVisibility(MangledEntry[] aTemplates, TxnState aTxn,
                                   RemoteEventListener aListener,
                                   long aLeaseTime,
                                   MarshalledObject aHandback,
                                   Registrar aRegistrar, boolean visibleOnly)
        throws IOException, RemoteException {
        long myFirstSeqNum;
        EventGenerator myGenerator;

        if (aTxn == null) {
            myGenerator =
                EventGeneratorFactory.get().newPersistentVisibility(aTemplates,
                                                                    aListener,
                                                                    aLeaseTime,
                                                                    aHandback,
                                                                    visibleOnly);
        } else {
            myGenerator =
                EventGeneratorFactory.get().newTransientVisibility(aTemplates,
                                                                   aListener,
                                                                   aLeaseTime,
                                                                   aHandback,
                                                                   aTxn,
                                                                   visibleOnly);
            
        }

        myFirstSeqNum = myGenerator.getStartSeqNum();
        aRegistrar.newRegistration(myGenerator.getSourceId(),
                                   myFirstSeqNum,
                                   new SpaceNotifyUID(myGenerator.getId()));
    }

    public void begin() {
        try {
            /*
             * Apply the jump before processing any events.  This will be
             * performed on the in-memory copy and placed on disk at the
             * next checkpoint.  In the meantime, we make sure correct state
             * is preserved using log records
             */
            EventGeneratorFactory.get().jumpSequenceNumbers();
        } catch (IOException anIOE) {
            theLogger.log(Level.SEVERE, "Failed to apply restart jump", anIOE);
        }

        BoundedLinkedQueue myQueue;
        
        if (QUEUE_BOUND == 0) {
            theLogger.log(Level.INFO, "Event queue bounding disabled");
            myQueue = new BoundedLinkedQueue(Integer.MAX_VALUE);
            theProcessors =
                new PooledExecutor(myQueue, NUM_PROCESSORS);
        } else {
            theLogger.log(Level.INFO, "Event queue bounding enabled");
            myQueue = new BoundedLinkedQueue(QUEUE_BOUND);
            theProcessors =
                new PooledExecutor(myQueue, NUM_PROCESSORS);
        }

        theProcessors.setMinimumPoolSize(NUM_PROCESSORS);
        // theProcessors.waitWhenBlocked();

        StatsBoard.get().add(new QueueStatGenerator("Events", myQueue));        
    }

    public void halt() {
        theProcessors.shutdownNow();

        synchronized(this) {
            theLogger.log(Level.INFO, "Processed: " + theEventCount);
        }
    }

    void dispatchImpl(DispatchTask aTask) {
        synchronized(this) {
            ++theEventCount;
        }

        switch (aTask.getEvent().getType()) {
            case QueueEvent.TRANSACTION_ENDED :
            case QueueEvent.ENTRY_WRITE :
            case QueueEvent.ENTRY_WRITTEN :
            case QueueEvent.ENTRY_VISIBLE :
            case QueueEvent.ENTRY_NOT_CONFLICTED : {
                iterateMatches(aTask);
                break;
            }
        }
    }
    
    private void iterateMatches(DispatchTask aTask) {
        QueueEvent myEvent = aTask.getEvent();
        long myCurrentTime = System.currentTimeMillis();
        QueueEvent.Context myContext = myEvent.getContext();
        MangledEntry myEntry = null;
        
        if (myContext != null)
            myEntry = myEvent.getContext().getEntry();
        
        try {
            Iterator myGenerators =
                EventGeneratorFactory.get().getGenerators();

            while (myGenerators.hasNext()) {
                EventGenerator myGenerator = (EventGenerator) myGenerators.next();

                if ((myGenerator.canSee(myEvent, myCurrentTime)) &&
                    (myGenerator.matches(myEntry))) {
                    theDispatcher.sendEvent(aTask, myGenerator);
                }
            }
        } catch (IOException anIOE) {
            theLogger.log(Level.SEVERE, "Couldn't recover generators from factory");
        } finally {
            aTask.enableResolve();
        }
    }
    
    
    public boolean renew(OID aOID, long anExpiry)
        throws IOException {

        return EventGeneratorFactory.get().renew(aOID, anExpiry);
    }

    public boolean cancel(OID aOID)
        throws IOException {

        return EventGeneratorFactory.get().cancel(aOID);
    }

	public void kill(OID anOID) throws IOException {
		EventGeneratorFactory.get().killTemplate(anOID);
	}
}