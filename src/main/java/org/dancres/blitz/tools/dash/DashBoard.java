package org.dancres.blitz.tools.dash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import java.awt.Rectangle;

import net.jini.space.JavaSpace05;
import org.dancres.blitz.remote.StatsAdmin;
import org.dancres.blitz.stats.InstanceCount;
import org.dancres.blitz.stats.MemoryStat;
import org.dancres.blitz.stats.OpStat;
import org.dancres.blitz.stats.Stat;
import org.dancres.blitz.stats.TxnStat;
import org.dancres.blitz.stats.BlockingOpsStat;

public class DashBoard extends javax.swing.JPanel {
    
    
    /** Creates new form DashBoard */
    public DashBoard(JFrame frame) {
        parent=frame;
        initComponents();
        memoryBar.setMaximum(100);
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        blockingTakeLb = new javax.swing.JLabel();
        blockingTakeCount = new javax.swing.JLabel();
        blockingReadLb = new javax.swing.JLabel();
        blockingReadCount = new javax.swing.JLabel();
        blockingOpButton = new javax.swing.JButton();
        takeCountLb = new javax.swing.JLabel();
        writeCountLb = new javax.swing.JLabel();
        readCountLb = new javax.swing.JLabel();
        instanceCount = new javax.swing.JLabel();
        txnCount = new javax.swing.JLabel();
        memoryCount = new javax.swing.JLabel();
        memoryBar = new javax.swing.JProgressBar();
        txnButton = new javax.swing.JButton();
        spaceOpButton = new javax.swing.JButton();
        instancesButton = new javax.swing.JButton();
        memoryButton = new javax.swing.JButton();
        rawButton = new javax.swing.JButton();
        takeCount = new javax.swing.JLabel();
        writeCount = new javax.swing.JLabel();
        readCount = new javax.swing.JLabel();

        setLayout(null);

        takeCountLb.setText("Take:");
        add(takeCountLb);
        takeCountLb.setBounds(160, 40, 40, 15);

        writeCountLb.setText("Write:");
        add(writeCountLb);
        writeCountLb.setBounds(160, 60, 40, 15);

        readCountLb.setText("Read:");
        add(readCountLb);
        readCountLb.setBounds(160, 80, 40, 15);

        blockingReadLb.setText("Read:");
        add(blockingReadLb);
        blockingReadLb.setBounds(300, 40, 40, 15);

        blockingTakeLb.setText("Take:");
        add(blockingTakeLb);
        blockingTakeLb.setBounds(300, 60, 40, 15);

        instanceCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        instanceCount.setText("0");
        add(instanceCount);
        instanceCount.setBounds(440, 40, 130, 15);

        txnCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txnCount.setText("0");
        add(txnCount);
        txnCount.setBounds(20, 40, 130, 15);

        memoryCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        memoryCount.setText("0");
        add(memoryCount);
        memoryCount.setBounds(580, 40, 140, 15);

        add(memoryBar);
        memoryBar.setBounds(580, 60, 140, 20);

        txnButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/txns.gif")));
        txnButton.setText("Active Txns");
        txnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txnButtonActionPerformed(evt);
            }
        });

        add(txnButton);
        txnButton.setBounds(10, 10, 130, 26);

        spaceOpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/ops.gif")));
        spaceOpButton.setText("Space Ops");
        spaceOpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spaceOpButtonActionPerformed(evt);
            }
        });

        add(spaceOpButton);
        spaceOpButton.setBounds(150, 10, 130, 26);

        blockingOpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/blocking.gif")));
        blockingOpButton.setText("Blocking");
        blockingOpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blockingButtonActionPerformed(evt);
            }
        });

        add(blockingOpButton);
        blockingOpButton.setBounds(290, 10, 130, 26);

        instancesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/instances.gif")));
        instancesButton.setText("Instances");
        instancesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instancesButtonActionPerformed(evt);
            }
        });

        add(instancesButton);
        instancesButton.setBounds(430, 10, 140, 26);

        memoryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/memory.gif")));
        memoryButton.setText("Memory");
        memoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memoryButtonActionPerformed(evt);
            }
        });

        add(memoryButton);
        memoryButton.setBounds(580, 10, 140, 26);

        rawButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("images/steak.gif")));
        rawButton.setText("Raw");
        rawButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rawButtonActionPerformed(evt);
            }
        });

        add(rawButton);
        rawButton.setBounds(720, 10, 140, 26);

        takeCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        takeCount.setText("0");
        add(takeCount);
        takeCount.setBounds(200, 40, 80, 15);

        writeCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        writeCount.setText("0");
        add(writeCount);
        writeCount.setBounds(200, 60, 80, 15);

        readCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        readCount.setText("0");
        add(readCount);
        readCount.setBounds(200, 80, 80, 15);

        blockingReadCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        blockingReadCount.setText("0");
        add(blockingReadCount);
        blockingReadCount.setBounds(340, 40, 80, 15);

        blockingTakeCount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        blockingTakeCount.setText("0");
        add(blockingTakeCount);
        blockingTakeCount.setBounds(340, 60, 80, 15);

    }//GEN-END:initComponents
    private Rectangle getNextWinBounds(){
         int windowCount=_openWindows.size()-1;
         Rectangle dashBounds=parent.getBounds();
         return new Rectangle(dashBounds.x+(windowCount*25),dashBounds.y+dashBounds.height+(windowCount*25),400,300);
         
    }
    private void blockingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blockingButtonActionPerformed
        // Add your handling code here:
        //JOptionPane.showMessageDialog(this,"Not implemented yet!");
        StatsFrame statsFrame=new StatsFrame(parent,"Blocking",StatsFrame.BLOCKERS);        
        _openWindows.add(statsFrame);
        statsFrame.setBounds(getNextWinBounds());
        statsFrame.setVisible(true);
    }//GEN-LAST:event_memoryButtonActionPerformed

    private void rawButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memoryButtonActionPerformed
        StatsFrame statsFrame=new StatsFrame(parent,"Raw",StatsFrame.RAW);
        _openWindows.add(statsFrame);
        statsFrame.setBounds(getNextWinBounds());
        statsFrame.setVisible(true);
    }

    private void memoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memoryButtonActionPerformed
        // Add your handling code here:
        //JOptionPane.showMessageDialog(this,"Not implemented yet!");
        StatsFrame statsFrame=new StatsFrame(parent,"Memory usage",StatsFrame.MEMORY);
        _openWindows.add(statsFrame);       
        statsFrame.setBounds(getNextWinBounds());
        statsFrame.setVisible(true);
    }//GEN-LAST:event_memoryButtonActionPerformed

    private void instancesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instancesButtonActionPerformed
        // Add your handling code here:
        //JOptionPane.showMessageDialog(this,"Not implemented yet!");
        StatsFrame statsFrame=new StatsFrame(parent,"Entry Instances",StatsFrame.INSTANCES,proxy);
        _openWindows.add(statsFrame);       
        statsFrame.setBounds(getNextWinBounds());
        statsFrame.setVisible(true);
    }//GEN-LAST:event_instancesButtonActionPerformed

    private void spaceOpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spaceOpButtonActionPerformed
        // Add your handling code here:
        StatsFrame statsFrame=new StatsFrame(parent,"Read/write/takes",StatsFrame.OPSTATS);
        _openWindows.add(statsFrame);
        statsFrame.setBounds(getNextWinBounds());
        statsFrame.setVisible(true);
    }//GEN-LAST:event_spaceOpButtonActionPerformed

    private void txnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txnButtonActionPerformed
        // Add your handling code here:
        StatsFrame statsFrame=new StatsFrame(parent,"Active Txns",StatsFrame.TXNS);
        _openWindows.add(statsFrame);
        statsFrame.setBounds(getNextWinBounds());
        statsFrame.setVisible(true);
    }//GEN-LAST:event_txnButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel blockingTakeLb;
    private javax.swing.JLabel blockingTakeCount;
    private javax.swing.JLabel blockingReadLb;
    private javax.swing.JLabel blockingReadCount;
    private javax.swing.JButton blockingOpButton;
    private javax.swing.JLabel instanceCount;
    private javax.swing.JButton instancesButton;
    private javax.swing.JButton rawButton;
    private javax.swing.JProgressBar memoryBar;
    private javax.swing.JButton memoryButton;
    private javax.swing.JLabel memoryCount;
    private javax.swing.JLabel readCount;
    private javax.swing.JLabel readCountLb;
    private javax.swing.JButton spaceOpButton;
    private javax.swing.JLabel takeCount;
    private javax.swing.JLabel takeCountLb;
    private javax.swing.JButton txnButton;
    private javax.swing.JLabel txnCount;
    private javax.swing.JLabel writeCount;
    private javax.swing.JLabel writeCountLb;
    // End of variables declaration//GEN-END:variables
    
    //blitz specific stuff
    //private Object [] data;
    private StatsAdmin statsAdmin;
    private JavaSpace05 proxy;
    private Map lookup;
    private ArrayList _openWindows=new ArrayList();
    private JFrame parent;
    
    void init(JavaSpace05 aProxy, StatsAdmin admin){
        proxy = aProxy;
        statsAdmin=admin;
    }
    private void updateWindows(Stat [] stats){
    
        ArrayList deathRow=new ArrayList();
        
        int n=_openWindows.size();
        for(int i=0;i<n;i++){
            UpdateableView win=(UpdateableView)_openWindows.get(i);
            boolean active=win.update(stats);
            if(!active){
                deathRow.add(win);
            }
        }
        //execute the prisoners
        n=deathRow.size();
        for(int i=0;i<n;i++){
            _openWindows.remove(deathRow.get(i));
        }
    }
    void update(Stat[] stats){
        
        updateWindows(stats);
        
        lookup=new HashMap();
        
        long readCounter=0;
        long writeCounter=0;
        long takeCounter=0;     
        long instanceCounter=0; 
                
        for(int i=0;i<stats.length;i++){
        
            if(stats[i] instanceof MemoryStat){
                MemoryStat ms=(MemoryStat)stats[i];
                double max=ms.getMaxMemory();
                double used=ms.getCurrentMemory();
                        
                double pc=used/max*100;
                double maxKB=max/(1024);
                double usedKB=used/(1024);
                
                memoryCount.setText(""+(int)usedKB+"/"+(int)maxKB+" Kb");
                memoryBar.setValue((int)pc);
                                
                
            }else if(stats[i] instanceof OpStat){
                OpStat op=(OpStat)stats[i];
                String type=op.getType();
                int theOp=op.getOp();
                Long count=new Long(op.getCount());
                        
                Object [] data=getData(type);
                
                data[0]=type;
                        
                switch(theOp){
                    
                    case OpStat.READS : data[2]=count;
                                        readCounter+=count.longValue();
                                        break;
                    case OpStat.WRITES :data[3]=count;
                                        writeCounter+=count.longValue();
                                        break;
                    case OpStat.TAKES : data[4]=count;
                                        takeCounter+=count.longValue();
                                        break;
                }
                        //data[1]=new Long( ((Long)data[3]).longValue()-((Long)data[3]).longValue());
            }else if (stats[i] instanceof InstanceCount) {
                InstanceCount myCount = (InstanceCount) stats[i];
                String type=myCount.getType();
                Object [] data=getData(type);
                data[0]=type;
                data[1]=new Integer(myCount.getCount());
                instanceCounter+=myCount.getCount();

            }else if (stats[i] instanceof TxnStat) {
                TxnStat myTxns = (TxnStat) stats[i];
                 txnCount.setText(""+myTxns.getActiveTxnCount());

            }else if (stats[i] instanceof BlockingOpsStat) {
                BlockingOpsStat myBlockers = (BlockingOpsStat) stats[i];
                blockingReadCount.setText(""+myBlockers.getReaders());
                blockingTakeCount.setText(""+myBlockers.getTakers());

            }
            
        }
        readCount.setText(""+readCounter);
        writeCount.setText(""+writeCounter);
        takeCount.setText(""+takeCounter);
        instanceCount.setText(""+instanceCounter);
    }
    private Object [] getData(String type){
        Object [] data=(Object[])lookup.get(type);
        if(data==null){
            data=new Object[]{"",new Long(0),new Long(0),new Long(0),new Long(0)};
            lookup.put(type,data);
        }
        return data;
    }
   
}
