package assembler;

import javax.swing.JFrame;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 'SIN
 */
public class PipelineFrame extends JFrame {

    PipelinePanel pp;

    PipelineFrame(String title, String ins) {
        super(title);
        pp = new PipelinePanel(ins);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(pp.getWidth() + 10, pp.getHeight() + 10);
        this.setVisible(true);
        this.add(pp);
        this.setBounds(100, 50, this.getWidth(), this.getHeight());
        this.setResizable(false);
    }
}
