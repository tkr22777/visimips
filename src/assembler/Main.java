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
/**
 *
 * @author 'SIN
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

            try {
                    // Set System L&F
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception e) {
               // handle exception
            }
        // TODO code application logic here       
        MainFrame mf = new MainFrame("ViSiMIPS");
    }
}
