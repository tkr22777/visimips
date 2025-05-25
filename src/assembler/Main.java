/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.SwingUtilities;

/**
 *
 * @author 'SIN
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Set system properties BEFORE creating any UI components
        // Java 17 compatible settings for macOS
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", "ViSiMIPS");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "ViSiMIPS");

        // Modern Java settings for better compatibility
        System.setProperty("java.awt.headless", "false");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Run on Event Dispatch Thread for better compatibility
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Use system Look & Feel for better native appearance with Java 17
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                } catch (Exception e) {
                    // Fallback to cross-platform L&F if system L&F fails
                    System.err.println("Could not set system look and feel: " + e.getMessage());
                    try {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } catch (Exception e2) {
                        System.err.println("Could not set cross-platform look and feel: " + e2.getMessage());
                    }
                }

                // Create and show the main frame
                MainFrame mf = new MainFrame();
            }
        });
    }
}
