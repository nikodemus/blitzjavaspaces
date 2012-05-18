/*
 * InstallPanel.java
 *
 * Created on 07 March 2004, 12:32
 */

package com.incax.install.blitz;

import javax.swing.*;
import java.io.*; 
import java.util.*;

/**
 *
 * @author  Phil
 */
public class InstallPanel extends javax.swing.JPanel {
    
    private static final String [] KEYS={"jdkHome","jiniHome",
                "instDir","port"};
    private JTextField [] TF;

    private InstallUtil _installer = new InstallUtil();

    /** Creates new form InstallPanel */
    public InstallPanel() {
        initComponents();
        TF=new JTextField[]{jdkHome,jiniHome,installDir,httpdPort};
        
        jdkHome.setText(System.getProperty("java.home"));
        if(_installer.isWindowsPlatform()){
            installDir.setText("c:\\blitz");
        }else{
            installDir.setText(System.getProperty("user.home")+"/blitz");
        }  
        jProgressBar1.setVisible(false);
        loadSettings();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jdkHome = new javax.swing.JTextField();
        jiniHome = new javax.swing.JTextField();
        httpdPort = new javax.swing.JTextField();
        installDir = new javax.swing.JTextField();
        jdkHomeSelect = new javax.swing.JButton();
        jiniHomeSelect = new javax.swing.JButton();
        installDirSelect = new javax.swing.JButton();
        installButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();

        setLayout(null);

        jLabel1.setText("JDK 1.6 home");
        add(jLabel1);
        jLabel1.setBounds(20, 10, 90, 16);

        jLabel2.setText("Jini 2.1 home");
        add(jLabel2);
        jLabel2.setBounds(20, 40, 80, 16);

        jLabel4.setText("HTTPD port");
        add(jLabel4);
        jLabel4.setBounds(20, 70, 70, 16);

        jLabel5.setText("Install DIR");
        add(jLabel5);
        jLabel5.setBounds(20, 100, 80, 16);

        add(jdkHome);
        jdkHome.setBounds(110, 10, 250, 20);

        add(jiniHome);
        jiniHome.setBounds(110, 40, 250, 20);

        httpdPort.setText("8085");
        add(httpdPort);
        httpdPort.setBounds(110, 70, 50, 20);

        add(installDir);
        installDir.setBounds(110, 100, 250, 20);

        jdkHomeSelect.setText("...");
        jdkHomeSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jdkHomeSelectActionPerformed(evt);
            }
        });

        add(jdkHomeSelect);
        jdkHomeSelect.setBounds(370, 10, 30, 20);

        jiniHomeSelect.setText("...");
        jiniHomeSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jiniHomeSelectActionPerformed(evt);
            }
        });

        add(jiniHomeSelect);
        jiniHomeSelect.setBounds(370, 40, 30, 20);

        installDirSelect.setText("...");
        installDirSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installDirSelectActionPerformed(evt);
            }
        });

        add(installDirSelect);
        installDirSelect.setBounds(370, 100, 30, 20);

        installButton.setText("Install");
        installButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installButtonActionPerformed(evt);
            }
        });

        add(installButton);
        installButton.setBounds(250, 160, 70, 26);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        add(cancelButton);
        cancelButton.setBounds(330, 160, 73, 26);

        add(jProgressBar1);
        jProgressBar1.setBounds(110, 164, 130, 20);

    }//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void installButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installButtonActionPerformed
        doInstall();
    }//GEN-LAST:event_installButtonActionPerformed

    private void installDirSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installDirSelectActionPerformed
        String path=getPath("Blitz install directory");
        if(path!=null){
            installDir.setText(path);
        }
    }//GEN-LAST:event_installDirSelectActionPerformed

    private void jiniHomeSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jiniHomeSelectActionPerformed
        String path=getPath("Jini 2.1 home directory");
        if(path!=null){
            jiniHome.setText(path);
        }
    }//GEN-LAST:event_jiniHomeSelectActionPerformed

    private void jdkHomeSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jdkHomeSelectActionPerformed
        String path=getPath("JDK 1.4 directory");
        if(path!=null){
            jdkHome.setText(path);
        }
    }//GEN-LAST:event_jdkHomeSelectActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField httpdPort;
    private javax.swing.JButton installButton;
    private javax.swing.JTextField installDir;
    private javax.swing.JButton installDirSelect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTextField jdkHome;
    private javax.swing.JButton jdkHomeSelect;
    private javax.swing.JTextField jiniHome;
    private javax.swing.JButton jiniHomeSelect;
    // End of variables declaration//GEN-END:variables
    private File _lastPath;
    
     public String getPath(String title){
        JFileChooser fileDlg=new JFileChooser(_lastPath);
        fileDlg.setDialogTitle(title);
                fileDlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        fileDlg.showOpenDialog(this);
        File file=fileDlg.getSelectedFile();
        if(file==null){
            return null;
        }
        if(file.exists()==false){
            JOptionPane.showMessageDialog(this,
                        "The selected directory does not exist");
            return null;
        }
        _lastPath=file;

        return file.getAbsolutePath();
    }
     private void doInstall(){
        try{
            saveSettings();
            
            if(!_installer.validateJiniHome(jiniHome.getText(), this)){
                return;
            }
            String strPort=httpdPort.getText();
            try{
                int port=Integer.parseInt(strPort);
                if(port<1100){
                    JOptionPane.showMessageDialog(this,
                                "Please select a port number >1099");
                    return;
                }
                if(port==8081){
                    JOptionPane.showMessageDialog(this,
                    "Port 8081 is reserved for use by some of the scripts generated by this installer\n"+
                    "Please select a different port number");
                    return;
                }
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Invalid port number");
                return;
            }
            jProgressBar1.setVisible(true);
            
            File instDir=new File(installDir.getText());
            
            String thisJar=_installer.getPathForClass( getClass() );
            _installer.extractZip(new File(thisJar),instDir, jProgressBar1);
            _installer.generateScripts(instDir, jdkHome.getText(),
                jiniHome.getText(),
                httpdPort.getText());
            
            JOptionPane.showMessageDialog(this,
                    "Blitz has succesfully been installed under "+
                                installDir.getText());
            System.exit(0);
        }catch(Exception ex){
            ex.printStackTrace(System.err);
            JOptionPane.showMessageDialog(this,ex);
        }
     }

    private void loadSettings(){
        try{
            Properties props=new Properties();
            FileInputStream fis=new FileInputStream(".install");
            props.load(fis);
            for(int i=0;i<KEYS.length;i++){
                String val=props.getProperty(KEYS[i]);
                if(val!=null){
                    TF[i].setText(val);
                }
            }
            
        }catch(Exception ex){
            //System.err.println(ex);
        }
    }
    private void saveSettings(){
        try{
            Properties props=new Properties();
            
            
            for(int i=0;i<KEYS.length;i++){
                props.setProperty(KEYS[i],TF[i].getText());                
            }
            FileOutputStream fos=new FileOutputStream(".install");
            props.save(fos, "Blitz install properties");
            fos.close();
            
        }catch(Exception ex){
            System.err.println(ex);
        }
    }
}
