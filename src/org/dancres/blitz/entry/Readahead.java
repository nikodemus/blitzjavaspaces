package org.dancres.blitz.entry;

import org.dancres.blitz.ActiveObject;
import org.dancres.blitz.ActiveObjectRegistry;
import org.dancres.blitz.Logging;
import org.dancres.blitz.mangler.MangledEntry;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;

import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;

/**
 */
public class Readahead implements ActiveObject {
    private static Logger theLogger =
        Logging.newLogger("org.dancres.blitz.entry.Readahead");

    private static Readahead theReadahead = new Readahead();

    private QueuedExecutor theExecutor;

    private Set theActiveTemplates = new HashSet();

    static Readahead get() {
        return theReadahead;
    }

    private Readahead() {
        ActiveObjectRegistry.add(this);
    }

    /**
     * @return <code>true</code> if the readahead will go active
     */
    boolean add(MangledEntry aTemplate, int aReadahead) {
        if ((! aTemplate.isSnapshot()) &&
            (! aTemplate.isWildcard())) {
            if (theLogger.isLoggable(Level.FINEST))
                theLogger.log(Level.FINEST,
                    "Readahead not snapshot/wildcard: " + aTemplate.getType());
            return false;
        }

        if (aReadahead == 0) {
            if (theLogger.isLoggable(Level.FINEST))
                theLogger.log(Level.FINEST,
                    "Readahead not set: " + aTemplate.getType());
            return false;
        }

        synchronized(theActiveTemplates) {
            if (! theActiveTemplates.contains(aTemplate)) {
                if (theLogger.isLoggable(Level.FINEST))
                    theLogger.log(Level.FINEST,
                        "Readahead going active: " + aReadahead);
                theActiveTemplates.add(aTemplate);

                try {
                    theExecutor.execute(new ReadTask(aTemplate, aReadahead));
                } catch (InterruptedException anIE) {
                    // Doesn't matter it's just a readahead
                }

                return true;
            } else
                return false;
        }
    }

    void remove(MangledEntry aTemplate) {
        synchronized(theActiveTemplates) {
            theActiveTemplates.remove(aTemplate);
        }
    }

    public void begin() {
        theExecutor = new QueuedExecutor();
    }

    public void halt() {
        theExecutor.shutdownNow();
    }

    static class ReadTask implements Runnable {
        private MangledEntry theTemplate;
        private int theReadahead;

        ReadTask(MangledEntry aTemplate, int aReadahead) {
            theTemplate = aTemplate;
            theReadahead = aReadahead;
        }

        public void run() {
            try {
                String myType = theTemplate.getType();

                EntryReposRecovery myRepos =
                        EntryRepositoryFactory.get().getAdmin(myType);

                // System.err.println("Attempting readahead of 10");

                myRepos.find(theTemplate, new CountingVisitor(theReadahead));
            } catch (IOException anIOE) {
                    theLogger.log(Level.SEVERE,
                            "Readahead failed with", anIOE);
            } finally {
                Readahead.get().remove(theTemplate);
            }
        }
    }

    static class CountingVisitor implements SearchVisitor {
        private int theRequiredOffers;
        private int theTotalOffers;

        CountingVisitor(int aRequired) {
            theRequiredOffers = aRequired;
        }

        public boolean isDeleter() {
            return false;
        }

        public int offer(SearchOffer anOffer) {
            ++theTotalOffers;

            if (theTotalOffers == theRequiredOffers)
                return STOP;
            else
                return TRY_AGAIN;
        }
    }
}