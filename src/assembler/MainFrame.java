/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package assembler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author 'SIN
 */
public class MainFrame extends JFrame {

    public JTextArea codingArea;
    JButton simBtn ;
    public MainFrame(String str)
    {
        super(str);
        String code = "addi $s1 , $zero , 50\n" +
                      "and  $s3 , $zero, $s1 #this is a comment :p\n" +
                      "addi $s4 , $zero, 50\n" +
                      "or   $t1 , $zero , $s3 \n" +
                      "nor  $t2 , $t1 , $s3 \n" +
                      "addi $t3 , $zero , 300 \n" +
                      "beq  $s4 , $s1 , 2 \n" +
                      "addi $t4 , $zero , 400 \n" +
                      "addi $t5 , $t4 , 500 \n" +
                      "sub  $t6 , $zero , $t1 \n" +
                      "illigal $t7 , $zero , $t6 \n" +
                      "addi $t8 , $zero , 800 \n" +
                      "sw   $t7 , 50($s1)\n" +
                      "lw   $t1 , 50($s1)\n" +
                      "add  $s7 , $t1, $t5\n" +
                      "sub  $k0 , $s7, $t1";

        this.codingArea = new JTextArea(code ,15,30);
        this.setLayout(null);

        JPanel jp = new JPanel();
        jp.setLayout(null);
        JScrollPane jsp = new JScrollPane(this.codingArea);
        jsp.setBounds(20,10,320,300);
        jp.setSize(600,400);
        jp.setBounds(0,0,jp.getWidth(),jp.getHeight());
        jp.add(jsp);
        simBtn = new JButton("Simulate");
        simBtn.setBounds(20,320,100,30);
        simBtn.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        new PipelineFrame("ViSiMiPS",codingArea.getText());
                        setVisible(false);
                        return;
                    }
                }
        );
        jp.add(simBtn);
        this.add(jp);
        this.setBounds(500,200,this.getWidth(),this.getHeight());
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(380,400);
        this.setVisible(true);
        this.setResizable(false);
    }
}
