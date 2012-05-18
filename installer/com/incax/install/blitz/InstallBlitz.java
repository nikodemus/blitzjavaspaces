/*
 * InstallBlitz.java
 *
 * Created on 07 March 2004, 12:43
 */

package com.incax.install.blitz;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 *
 * @author  Phil
 */
public class InstallBlitz {

    private static final String TITLE="Blitz Installer";
    private static final String VERSION="1.0";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Performing GUI install");

            String lfClassName = UIManager.getSystemLookAndFeelClassName();
            String uiClass = "javax.swing.plaf.metal.MetalLookAndFeel";
            if (uiClass.equals(lfClassName)) {
                javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new Theme());
            }
            try {
                UIManager.setLookAndFeel(lfClassName);
            } catch (Exception ex) {
            }

            JFrame f = new JFrame(TITLE + " Version " + VERSION);
            f.setResizable(false);
            f.getContentPane().add(new InstallPanel(), BorderLayout.CENTER);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(430, 230);
            centerFrame(f);
            f.setVisible(true);
        } else {
            System.out.println("Performing headless install");

            if (args.length == 3) {
                doHeadlessInstall(args[0], args[1],
                    System.getProperty("java.home"),
                    args[2]);
            } else if (args.length == 4) {
                doHeadlessInstall(args[1], args[2],
                    args[0],
                    args[3]);
            } else {
                System.err.println("Arguments should be either:");
                System.err.println("JDK_Home Jini_Home Install_Dir HTTP_Port");
                System.err.println("or");
                System.err.println("Jini_Home Install_Dir HTTP_Port");
            }
        }
    }
    private static void centerFrame(Frame f){
        Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size=f.getSize();

        int xpos=(screen.width/2)-(size.width/2);
        int ypos=(screen.height/2)-(size.height/2);
        f.setLocation(xpos,ypos);
    }

    private static void doHeadlessInstall(String aJiniHome, String anInstallDir,
                                          String aJdkDir,
                                          String aPortNumber) throws Exception {

        InstallUtil myInstaller = new InstallUtil();

        if (!myInstaller.validateJiniHome(aJiniHome, null)) {
            return;
        }
        try {
            int port = Integer.parseInt(aPortNumber);
            if (port < 1100) {
                System.err.println("Please choose a port number >1099");
                return;
            }
            if (port == 8081) {
                System.err.println("Port 8081 is reserved for use by some of the scripts generated by this installer\n" +
                        "Please choose a different port number");
                return;
            }
        } catch (Exception ex) {
            System.err.println("Invalid port number");
            return;
        }

        File instDir = new File(anInstallDir);

        String thisJar = myInstaller.getPathForClass(InstallBlitz.class);
        myInstaller.extractZip(new File(thisJar), instDir, null);
        myInstaller.generateScripts(instDir, aJdkDir,
            aJiniHome, aPortNumber);

        System.out.println("Blitz has succesfully been installed under " +
                anInstallDir);
        System.exit(0);
    }
}
