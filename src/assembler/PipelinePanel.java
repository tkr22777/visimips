/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author 'SIN
 */
public class PipelinePanel extends JPanel {

    int height = 648;
    int width = 1024;
    int wireNumber = 76;
    WireLabel wire[] = new WireLabel[wireNumber];
    MFrame Frames[];
    MIPS mips;
    int m;
    JButton nBtn, pBtn, modeChange;
    JComboBox comboBox;
    MouseEventHandler mHandler;
    int mode = 1;
    String comboText[] = {" Decimal", " Binary", " Help"};

    int x;
    int y;
    int tempLastWireSelect = 0;
    public Color activeWColor = Color.BLUE;
    public Color normalWColor = Color.BLACK;
    public Color elementColor = Color.DARK_GRAY;
    public BasicStroke elementStroke = new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public BasicStroke subEelementStroke = new BasicStroke(2.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public BasicStroke miniEelementStroke = new BasicStroke(1.7f);
    public BasicStroke wireStroke = new BasicStroke(1.0f);
    String[] Registers = {"$zero", "$at", "$v0", "$v1", "$a0", "$a1", "$a2",
        "$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6",
        "$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6",
        "$s7", "$t8", "$t9", "$k0", "$k1", "$gp", "$sp", "$fp",
        "$ra"};

    PipelinePanel(String ins) {

        this.setSize(width, height + 15);
        mips = new MIPS();
        Frames = mips.execute(ins);
        mHandler = new MouseEventHandler();
        m = 0;

        this.initializeWires();

        comboBox = new JComboBox(comboText);
        comboBox.setBounds(79, 560, 80, 30);
        comboBox.addItemListener(
                new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {

                    mode = comboBox.getSelectedIndex() + 1;
                }
            }
        }
        );
        this.add(comboBox);

        nBtn = new JButton("Next Clock Pulse >");
        nBtn.setBounds(20, 480, 140, 30);
        nBtn.addMouseListener(mHandler);
        this.add(nBtn);

        pBtn = new JButton("< Previous Clock Pulse");
        pBtn.setBounds(20, 520, 140, 30);
        pBtn.addMouseListener(mHandler);
        this.add(pBtn);

        this.addMouseMotionListener(mHandler);
        this.addMouseListener(mHandler);
        this.addMouseWheelListener(mHandler);
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        this.setLayout(null);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.drawElements(g2d);

        Font font = new Font("Lucida Sans", Font.PLAIN, convX(20) < convY(20) ? convX(20) : convY(20));
        g2d.setFont(font);

        g2d.setColor(Color.RED);
        int q = this.getValueOfWire().length();

        int a = x + 10, b = Math.max(y - 5, 42);

        a = Math.min(a, 960 - (q - 1) * 12);

        //    g2d.drawString("" + getValueOfWire() , x + 5 , y - 5);
        //  g2d.drawString("" + this.getValueOfWire() , 20 ,600);
        g2d.drawString("" + this.getValueOfWire(), a, b);
        g2d.setColor(Color.BLACK);

        this.drawText(g2d);

        for (int i = 0; i < this.wire.length; i++) {
            add(wire[i]);
        }

    }

    private void drawElements(Graphics2D g2d) {
        Font font = new Font("Arial", Font.BOLD, convX(12) < convY(12) ? convX(12) : convY(12));
        g2d.setFont(font);
        g2d.setColor(this.elementColor);
        g2d.setStroke(this.elementStroke);

        g2d.draw(PC);
        g2d.drawString("PC", PC.x + 5, PC.y + PC.height / 2 + 5);

        g2d.draw(insMem);
        g2d.drawString("Instruction", insMem.x + 11, insMem.y + insMem.height / 2);
        g2d.drawString("  Memory", insMem.x + 11, insMem.y + insMem.height / 2 + font.getSize() + 2);

        g2d.draw(regFile);
        g2d.drawString("Register", regFile.x + 18, regFile.y + regFile.height / 2);
        g2d.drawString("    File", regFile.x + 18, regFile.y + regFile.height / 2 + font.getSize() + 3);

        g2d.draw(hazUnit);
        g2d.drawString(" Hazard Detection", hazUnit.x + 5, hazUnit.y + hazUnit.height / 2);
        g2d.drawString("             Unit", hazUnit.x + 5, hazUnit.y + hazUnit.height / 2 + font.getSize() + 3);

        g2d.draw(memUnit);
        g2d.drawString("Memory", memUnit.x + 18, memUnit.y + memUnit.height / 2);

        g2d.draw(fwdUnit);
        g2d.drawString("Forwarding", fwdUnit.x + 22, fwdUnit.y + fwdUnit.height / 2);
        g2d.drawString("      Unit", fwdUnit.x + 22, fwdUnit.y + fwdUnit.height / 2 + font.getSize() + 3);

        int tx[] = aluX;
        int ty[] = aluY;
        g2d.drawPolyline(tx, ty, 8);
        g2d.drawString("ALU", tx[5] + convX(6), ty[5] + convY(5));

        g2d.setStroke(this.subEelementStroke);

        g2d.drawOval(cUnit.x, cUnit.y, cUnit.width, cUnit.height);
        g2d.drawString("Control", cUnit.x + 7, cUnit.y + cUnit.height / 2 + 4);

        font = new Font("Arial", Font.BOLD, convX(10) < convY(10) ? convX(10) : convY(10));
        g2d.setFont(font);

        g2d.draw(if_Id);

        g2d.draw(id_Ex);
        g2d.drawLine(id_Ex.x, id_Ex.y + (int) (convY(24) * 1.2),
                id_Ex.x + convX(24), id_Ex.y + (int) (convY(24) * 1.2));
        g2d.drawLine(id_Ex.x, id_Ex.y + (int) (convY(24) * 1.2) * 2,
                id_Ex.x + convX(24), id_Ex.y + (int) (convY(24) * 1.2) * 2);
        g2d.drawLine(id_Ex.x, id_Ex.y + (int) (convY(24) * 1.2) * 3,
                id_Ex.x + convX(24), id_Ex.y + (int) (convY(24) * 1.2) * 3);

        g2d.drawString("WB", id_Ex.x + convX(5), id_Ex.y + (int) (convY(24) * 1.2) - convY(10));
        g2d.drawString("M", id_Ex.x + convX(8), id_Ex.y + (int) (convY(24) * 1.2) * 2 - convY(10));
        g2d.drawString("EX", id_Ex.x + convX(6), id_Ex.y + (int) (convY(24) * 1.2) * 3 - convY(10));

        g2d.draw(ex_Mem);
        g2d.drawLine(ex_Mem.x, ex_Mem.y + (int) (convY(24) * 1.2),
                ex_Mem.x + convX(24), ex_Mem.y + (int) (convY(24) * 1.2));
        g2d.drawLine(ex_Mem.x, ex_Mem.y + (int) (convY(24) * 1.2) * 2,
                ex_Mem.x + convX(24), ex_Mem.y + (int) (convY(24) * 1.2) * 2);

        g2d.drawString("WB", ex_Mem.x + convX(5), ex_Mem.y + (int) (convY(24) * 1.2) - 10);
        g2d.drawString("M", ex_Mem.x + convX(8), ex_Mem.y + (int) (convY(24) * 1.2) * 2 - 10);

        g2d.draw(mem_Wb);
        g2d.drawLine(mem_Wb.x, mem_Wb.y + (int) (convY(24) * 1.2),
                mem_Wb.x + convX(24), mem_Wb.y + (int) (convY(24) * 1.2));
        g2d.drawString("WB", mem_Wb.x + convX(5), mem_Wb.y + (int) (convY(24) * 1.2) - 10);

        g2d.setStroke(this.miniEelementStroke);

        g2d.drawRoundRect(op1Mux.x, op1Mux.y, op1Mux.width, op1Mux.height, 20, 10);
        g2d.drawString("MUX", op1Mux.x + convX(15), op1Mux.y + op1Mux.height / 2 + 4);
        g2d.drawString("0", op1Mux.x + convX(3), op1Mux.y + 1 * op1Mux.height / 3 - convY(6));
        g2d.drawString("1", op1Mux.x + convX(3), op1Mux.y + 2 * op1Mux.height / 3 - convY(6));
        g2d.drawString("2", op1Mux.x + convX(3), op1Mux.y + 3 * op1Mux.height / 3 - convY(6));

        g2d.drawRoundRect(op2Mux.x, op2Mux.y, op2Mux.width, op2Mux.height, 20, 10);
        g2d.drawString("MUX", op2Mux.x + convX(15), op2Mux.y + op2Mux.height / 2 + 4);
        g2d.drawString("0", op2Mux.x + convX(3), op2Mux.y + 1 * op2Mux.height / 3 - convY(6));
        g2d.drawString("1", op2Mux.x + convX(3), op2Mux.y + 2 * op2Mux.height / 3 - convY(6));
        g2d.drawString("2", op2Mux.x + convX(3), op2Mux.y + 3 * op2Mux.height / 3 - convY(6));

        g2d.drawRoundRect(pcMux.x, pcMux.y, pcMux.width, pcMux.height, convX(20), convY(10));
        g2d.drawString("MUX", pcMux.x + convX(10), pcMux.y + pcMux.height / 2 + 4);
        g2d.drawString("1", pcMux.x + convX(4), pcMux.y + 1 * pcMux.height / 3 + 1);
        g2d.drawString("0", pcMux.x + convX(4), pcMux.y + 2 * pcMux.height / 3 + 8);

        g2d.drawRoundRect(wbMux.x, wbMux.y, wbMux.width, wbMux.height, convX(20), convY(10));
        g2d.drawString("MUX", wbMux.x + convX(10), wbMux.y + wbMux.height / 2 + 4);
        g2d.drawString("1", wbMux.x + convX(4), wbMux.y + 1 * wbMux.height / 3 + 1);
        g2d.drawString("0", wbMux.x + convX(4), wbMux.y + 2 * wbMux.height / 3 + 8);

        g2d.drawOval(signEx.x, signEx.y, signEx.width, signEx.height);
        g2d.drawString("Sign", signEx.x + convX(7), signEx.y + signEx.height / 2 - convY(4));
        g2d.drawString("Ex-32", signEx.x + convX(4), signEx.y + signEx.height / 2 + font.getSize());

        g2d.drawRoundRect(op22Mux.x, op22Mux.y, op22Mux.width, op22Mux.height, 20, 10);
        g2d.drawString("MUX", op22Mux.x + convX(13), op22Mux.y + op22Mux.height / 2 + 4);
        g2d.drawString("0", op22Mux.x + convX(4), op22Mux.y + 1 * op22Mux.height / 3 + convY(6));
        g2d.drawString("1", op22Mux.x + convX(4), op22Mux.y + 2 * op22Mux.height / 3 + convY(5));

        g2d.drawRoundRect(desMux.x, desMux.y, desMux.width, desMux.height, 20, 10);
        g2d.drawString("MUX", desMux.x + convX(10), desMux.y + desMux.height / 2 + convX(4));
        g2d.drawString("0", desMux.x + convX(4), desMux.y + 1 * desMux.height / 3 + convY(3));
        g2d.drawString("1", desMux.x + convX(4), desMux.y + 2 * desMux.height / 3 + convY(5));

        g2d.setStroke(wireStroke);

        tx = pcAddX;
        ty = pcAddY;
        g2d.drawPolyline(tx, ty, 8);
        g2d.drawString("4", this.wire[3].x[0] - 10, (pcAddY[3] + pcAddY[4]) / 2 + font.getSize() / 2);

        g2d.drawOval(aluControl.x, aluControl.y, aluControl.width, aluControl.height);
        g2d.drawString("ALUOp", aluControl.x + 1, aluControl.y + aluControl.height / 2 + 4);

        g2d.drawOval(shiftLeft.x, shiftLeft.y, shiftLeft.width, shiftLeft.height);
        g2d.drawString("Sft.L", shiftLeft.x + 1, shiftLeft.y + shiftLeft.height / 2 + 4);

        tx = brunchAddX;
        ty = brunchAddY;
        g2d.drawPolyline(tx, ty, 8);

        g2d.drawArc(andArc.x, andArc.y, andArc.width, andArc.height, 270, 180);
        g2d.drawLine(andArc.x + andArc.width / 2, andArc.y, andArc.x + andArc.width / 2, andArc.y + andArc.height);

    }

    private void initializeWires() {

        String value = "";

        int w0x[] = {this.PC.x, this.PC.x - convX(10), this.PC.x - convX(10), this.pcMux.x
            + this.pcMux.width + convX(10), this.pcMux.x + this.pcMux.width + convX(10),
            this.pcMux.x + this.pcMux.width};
        int w0y[] = {this.PC.y + this.PC.height / 2, this.PC.y + this.PC.height / 2,
            this.pcMux.y - convX(10), this.pcMux.y - convX(10),
            this.pcMux.y + this.pcMux.height / 2, this.pcMux.y + this.pcMux.height / 2};
        wire[0] = new WireLabel(w0x, w0y);

        int w1x[] = {this.PC.x + this.PC.width / 2, this.PC.x + this.PC.width / 2,
            this.hazUnit.x};
        int w1y[] = {this.PC.y, this.hazUnit.y + this.hazUnit.height / 2,
            this.hazUnit.y + this.hazUnit.height / 2};
        wire[1] = new WireLabel(w1x, w1y);

        int w2x[] = {this.PC.x + this.PC.width, this.insMem.x,
            (this.PC.x + this.PC.width + this.insMem.x) / 2,
            (this.PC.x + this.PC.width + this.insMem.x) / 2, this.pcAddX[0]};
        int w2y[] = {this.PC.y + this.PC.height / 2, this.PC.y + this.PC.height / 2,
            this.PC.y + this.PC.height / 2, (this.pcAddY[0] + this.pcAddY[this.pcAddY.length - 2]) / 2,
            (this.pcAddY[0] + this.pcAddY[this.pcAddY.length - 2]) / 2};
        wire[2] = new WireLabel(w2x, w2y);

        int w3x[] = {this.pcAddX[3] - convX(10), this.pcAddX[3]};
        int w3y[] = {(this.pcAddY[3] + this.pcAddY[4]) / 2, (this.pcAddY[3] + this.pcAddY[4]) / 2};
        wire[3] = new WireLabel(w3x, w3y);

        int w4x[] = {this.pcMux.x, this.pcMux.x - convX(15), this.pcMux.x - convX(15),
            this.pcAddX[1] + convX(15), this.pcAddX[1] + convX(15),
            this.pcAddX[1], this.if_Id.x};
        int w4y[] = {this.pcMux.y + (int) (this.pcMux.height * 2 / 3 + convY(4)),
            this.pcMux.y + (int) (this.pcMux.height * 2 / 3 + convY(4)),
            (this.pcMux.y + this.pcMux.height + this.pcAddY[0]) / 2,
            (this.pcMux.y + this.pcMux.height + this.pcAddY[0]) / 2,
            (this.pcAddY[1] + this.pcAddY[2]) / 2,
            (this.pcAddY[1] + this.pcAddY[2]) / 2,
            (this.pcAddY[1] + this.pcAddY[2]) / 2};
        wire[4] = new WireLabel(w4x, w4y);

        int w5x[] = {this.pcMux.x, this.pcMux.x - convX(15), this.pcMux.x - convX(15),
            this.ex_Mem.x + this.ex_Mem.width + convX(15),
            this.ex_Mem.x + this.ex_Mem.width + convX(15),
            this.ex_Mem.x + this.ex_Mem.width};
        int w5y[] = {this.pcMux.y + (int) (this.pcMux.height * 1 / 3 - convY(4)),
            this.pcMux.y + (int) (this.pcMux.height * 1 / 3 - convY(4)),
            this.hazUnit.y - convY(10), this.hazUnit.y - convY(10),
            (this.brunchAddY[1] + this.brunchAddY[2]) / 2,
            (this.brunchAddY[1] + this.brunchAddY[2]) / 2};
        wire[5] = new WireLabel(w5x, w5y);

        int w6x[] = {this.pcMux.x + this.pcMux.width / 2, this.pcMux.x + this.pcMux.width / 2,
            this.andArc.x + this.andArc.width + convX(10),
            this.andArc.x + this.andArc.width + convX(10),
            this.andArc.x + this.andArc.width};
        int w6y[] = {this.pcMux.y, this.hazUnit.y - convY(20),
            this.hazUnit.y - convY(20), this.andArc.y + this.andArc.height / 2,
            this.andArc.y + this.andArc.height / 2};
        wire[6] = new WireLabel(w6x, w6y);

        int w7x[] = {this.insMem.x + this.insMem.width, this.if_Id.x};
        int w7y[] = {this.insMem.y + this.insMem.height / 2, this.insMem.y + this.insMem.height / 2};
        wire[7] = new WireLabel(w7x, w7y);

        int w8x[] = {this.if_Id.x + this.if_Id.width, this.id_Ex.x};
        int w8y[] = {(this.pcAddY[1] + this.pcAddY[2]) / 2, (this.pcAddY[1] + this.pcAddY[2]) / 2};
        wire[8] = new WireLabel(w8x, w8y);

        int w9x[] = {this.hazUnit.x + convX(10), this.hazUnit.x + convX(10),
            this.if_Id.x + this.if_Id.width, this.hazUnit.x + convX(10),
            this.hazUnit.x + convX(10)};
        int w9y[] = {this.hazUnit.y + this.hazUnit.height + convY(20), this.insMem.y + this.insMem.height / 2,
            this.insMem.y + this.insMem.height / 2, this.insMem.y + this.insMem.height / 2,
            this.desMux.y + this.desMux.height * 2 / 3};
        wire[9] = new WireLabel(w9x, w9y);

        int w10x[] = {this.wire[9].x[0], this.cUnit.x};
        int w10y[] = {this.cUnit.y + this.cUnit.height / 2, this.cUnit.y + this.cUnit.height / 2};
        wire[10] = new WireLabel(w10x, w10y);

        int w11x[] = {this.hazUnit.x + convX(22), this.hazUnit.x + convX(22), this.hazUnit.x + convX(10)};
        int w11y[] = {this.hazUnit.y + this.hazUnit.height, this.hazUnit.y + this.hazUnit.height + convY(20),
            this.hazUnit.y + this.hazUnit.height + convY(20)};
        wire[11] = new WireLabel(w11x, w11y);

        int w12x[] = {this.wire[9].x[0], this.regFile.x};
        int w12y[] = {this.regFile.y + convY(15), this.regFile.y + convY(15)};
        wire[12] = new WireLabel(w12x, w12y);

        int w13x[] = {this.wire[9].x[0], this.regFile.x};
        int w13y[] = {this.regFile.y + convY(30), this.regFile.y + convY(30)};
        wire[13] = new WireLabel(w13x, w13y);

        int w14x[] = {this.regFile.x, this.regFile.x - convX(20),
            this.regFile.x - convX(20),
            this.wbMux.x + this.wbMux.width + convX(10),
            this.wbMux.x + this.wbMux.width + convX(10),
            this.wbMux.x + this.wbMux.width};
        int w14y[] = {this.regFile.y + this.regFile.height - convY(20),
            this.regFile.y + this.regFile.height - convY(20),
            this.fwdUnit.y + this.fwdUnit.height + convX(27),
            this.fwdUnit.y + this.fwdUnit.height + convX(27),
            this.wbMux.y + this.wbMux.height / 2, this.wbMux.y + this.wbMux.height / 2};
        wire[14] = new WireLabel(w14x, w14y);

        int w15x[] = {this.regFile.x, this.regFile.x - convX(10),
            this.regFile.x - convX(10),
            this.mem_Wb.x + this.mem_Wb.width + convX(10),
            this.mem_Wb.x + this.mem_Wb.width + convX(10),
            this.mem_Wb.x + this.mem_Wb.width};
        int w15y[] = {this.regFile.y + this.regFile.height - convY(10),
            this.regFile.y + this.regFile.height - convY(10),
            this.fwdUnit.y + this.fwdUnit.height + convY(18),
            this.fwdUnit.y + this.fwdUnit.height + convY(18),
            this.desMux.y + this.desMux.height / 2,
            this.desMux.y + this.desMux.height / 2};
        wire[15] = new WireLabel(w15x, w15y);

        int w16x[] = {this.wire[9].x[0], this.signEx.x};
        int w16y[] = {this.signEx.y + this.signEx.height / 2,
            this.signEx.y + this.signEx.height / 2};
        wire[16] = new WireLabel(w16x, w16y);

        int w17x[] = {this.signEx.x + this.signEx.width, this.id_Ex.x};
        int w17y[] = {this.signEx.y + this.signEx.height / 2,
            this.signEx.y + this.signEx.height / 2};
        wire[17] = new WireLabel(w17x, w17y);

        int w18x[] = {this.wire[9].x[0], this.id_Ex.x};
        int w18y[] = {this.desMux.y - this.desMux.height * 1 / 3,
            this.desMux.y - this.desMux.height * 1 / 3};
        wire[18] = new WireLabel(w18x, w18y);

        int w19x[] = {this.wire[9].x[0], this.id_Ex.x};
        int w19y[] = {this.desMux.y, this.desMux.y};
        wire[19] = new WireLabel(w19x, w19y);

        int w20x[] = {this.wire[9].x[0], this.id_Ex.x};
        int w20y[] = {this.desMux.y + this.desMux.height * 1 / 3,
            this.desMux.y + this.desMux.height * 1 / 3};
        wire[20] = new WireLabel(w20x, w20y);

        int w21x[] = {this.wire[9].x[0], this.id_Ex.x};
        int w21y[] = {this.desMux.y + this.desMux.height * 2 / 3,
            this.desMux.y + this.desMux.height * 2 / 3};
        wire[21] = new WireLabel(w21x, w21y);

        int w22x[] = {this.regFile.x + this.regFile.width - convX(10),
            this.regFile.x + this.regFile.width - convX(10),
            this.mem_Wb.x + this.mem_Wb.width + convX(10),
            this.mem_Wb.x + this.mem_Wb.width + convX(10),
            this.mem_Wb.x + this.mem_Wb.width,
            this.wbMux.x + this.wbMux.width + convX(20),
            this.wbMux.x + this.wbMux.width + convX(20),
            this.fwdUnit.x + this.fwdUnit.width};
        int w22y[] = {this.regFile.y, this.mem_Wb.y - convY(10),
            this.mem_Wb.y - convY(10), this.mem_Wb.y + convY(10),
            this.mem_Wb.y + convY(10), this.mem_Wb.y + convY(10),
            this.fwdUnit.y + this.fwdUnit.height * 5 / 6,
            this.fwdUnit.y + this.fwdUnit.height * 5 / 6};
        wire[22] = new WireLabel(w22x, w22y);

        int w23x[] = {this.cUnit.x + convX(50), this.id_Ex.x};
        int w23y[] = {this.id_Ex.y + convY(15), this.id_Ex.y + convY(15)};
        wire[23] = new WireLabel(w23x, w23y);

        int w24x[] = {this.cUnit.x + convX(55), this.id_Ex.x};
        int w24y[] = {this.id_Ex.y + convY(42), this.id_Ex.y + convY(42)};
        wire[24] = new WireLabel(w24x, w24y);

        int w25x[] = {this.cUnit.x + convX(45), this.id_Ex.x};
        int w25y[] = {this.id_Ex.y + convY(70), this.id_Ex.y + convY(70)};
        wire[25] = new WireLabel(w25x, w25y);

        int w26x[] = {this.regFile.x + this.regFile.width, this.id_Ex.x};
        int w26y[] = {this.regFile.y + convY(10), this.regFile.y + convY(10)};
        wire[26] = new WireLabel(w26x, w26y);

        int w27x[] = {this.regFile.x + this.regFile.width, this.id_Ex.x};
        int w27y[] = {this.regFile.y + this.regFile.height - convY(15),
            this.regFile.y + this.regFile.height - convY(15)};
        wire[27] = new WireLabel(w27x, w27y);

        int w28x[] = {this.wire[9].x[0], this.wire[9].x[0]};
        int w28y[] = {this.wire[9].y[0] - convY(20), this.wire[9].y[0]};
        wire[28] = new WireLabel(w28x, w28y);

        int w29x[] = {this.id_Ex.x + this.id_Ex.width, this.ex_Mem.x};
        int w29y[] = {this.id_Ex.y + convY(42), this.id_Ex.y + convY(42)};
        wire[29] = new WireLabel(w29x, w29y);

        int w30x[] = {this.hazUnit.x + this.hazUnit.width,
            this.ex_Mem.x + this.ex_Mem.width / 2,
            this.ex_Mem.x + this.ex_Mem.width / 2};
        int w30y[] = {this.hazUnit.y + this.hazUnit.height * 1 / 4,
            this.hazUnit.y + this.hazUnit.height * 1 / 4,
            this.ex_Mem.y};
        wire[30] = new WireLabel(w30x, w30y);

        int w31x[] = {this.hazUnit.x + this.hazUnit.width,
            this.id_Ex.x + this.id_Ex.width / 2,
            this.id_Ex.x + this.id_Ex.width / 2};
        int w31y[] = {this.hazUnit.y + this.hazUnit.height * 3 / 4,
            this.hazUnit.y + this.hazUnit.height * 3 / 4,
            this.id_Ex.y};
        wire[31] = new WireLabel(w31x, w31y);

        int w32x[] = {this.id_Ex.x + this.id_Ex.width, this.ex_Mem.x};
        int w32y[] = {this.id_Ex.y + convY(15), this.id_Ex.y + convY(15)};
        wire[32] = new WireLabel(w32x, w32y);

        int w33x[] = {this.id_Ex.x + this.id_Ex.width,
            this.aluX[0] - convX(30),
            this.aluX[0] - convX(30), this.aluControl.x + convX(2)};
        int w33y[] = {this.id_Ex.y + convY(62), this.id_Ex.y + convY(62),
            this.aluControl.y + convY(15), this.aluControl.y + convY(15)};
        wire[33] = new WireLabel(w33x, w33y);

        int w34x[] = {this.id_Ex.x + this.id_Ex.width,
            this.desMux.x + this.desMux.width,
            this.desMux.x + this.desMux.width,
            this.desMux.x + this.desMux.width / 2,
            this.desMux.x + this.desMux.width / 2};
        int w34y[] = {this.id_Ex.y + convY(78), this.id_Ex.y + convY(78),
            this.desMux.y - convY(10), this.desMux.y - convY(10),
            this.desMux.y};
        wire[34] = new WireLabel(w34x, w34y);

        int w35x[] = {this.id_Ex.x + this.id_Ex.width,
            this.op22Mux.x + this.op22Mux.width / 2,
            this.op22Mux.x + this.op22Mux.width / 2};
        int w35y[] = {this.id_Ex.y + convY(70), this.id_Ex.y + convY(70),
            this.op22Mux.y};
        wire[35] = new WireLabel(w35x, w35y);

        int w36x[] = {this.id_Ex.x + this.id_Ex.width, this.brunchAddX[0]};
        int w36y[] = {(this.pcAddY[1] + this.pcAddY[2]) / 2,
            (this.pcAddY[1] + this.pcAddY[2]) / 2};
        wire[36] = new WireLabel(w36x, w36y);

        int w37x[] = {this.brunchAddX[1], this.ex_Mem.x};
        int w37y[] = {(this.brunchAddY[1] + this.brunchAddY[2]) / 2,
            (this.brunchAddY[1] + this.brunchAddY[2]) / 2};
        wire[37] = new WireLabel(w37x, w37y);

        int w38x[] = {this.id_Ex.x + this.id_Ex.width, this.op1Mux.x};
        int w38y[] = {this.wire[26].y[0], this.wire[26].y[0]};
        wire[38] = new WireLabel(w38x, w38y);

        int w39x[] = {this.op1Mux.x, this.op1Mux.x - convX(30),
            this.op1Mux.x - convX(30), this.op2Mux.x,
            this.op1Mux.x - convX(30), this.op1Mux.x - convX(30)};
        int w39y[] = {this.op1Mux.y + this.op1Mux.height / 2,
            this.op1Mux.y + this.op1Mux.height / 2,
            this.op2Mux.y + this.op2Mux.height / 2,
            this.op2Mux.y + this.op2Mux.height / 2,
            this.op2Mux.y + this.op2Mux.height / 2,
            this.wire[14].y[2]};
        wire[39] = new WireLabel(w39x, w39y);

        int w40x[] = {this.op1Mux.x, this.op1Mux.x - convX(15),
            this.op1Mux.x - convX(15), this.op2Mux.x,
            this.op1Mux.x - convX(15), this.op1Mux.x - convX(15),
            this.memUnit.x + convX(20), this.memUnit.x + convX(20)};
        int w40y[] = {this.op1Mux.y + this.op1Mux.height - convY(10),
            this.op1Mux.y + this.op1Mux.height - convY(10),
            this.op2Mux.y + this.op2Mux.height - convY(10),
            this.op2Mux.y + this.op2Mux.height - convY(10),
            this.op2Mux.y + this.op2Mux.height - convY(10),
            this.fwdUnit.y + this.fwdUnit.height + convY(9),
            this.fwdUnit.y + this.fwdUnit.height + convY(9),
            this.memUnit.y + this.memUnit.height + convY(20)};
        wire[40] = new WireLabel(w40x, w40y);

        int w41x[] = {this.id_Ex.x + this.id_Ex.width, this.op2Mux.x};
        int w41y[] = {this.wire[27].y[0], this.wire[27].y[0]};
        wire[41] = new WireLabel(w41x, w41y);

        int w42x[] = {this.id_Ex.x + this.id_Ex.width,
            this.shiftLeft.x + this.shiftLeft.width / 2,
            this.shiftLeft.x + this.shiftLeft.width / 2,
            this.shiftLeft.x + this.shiftLeft.width / 2,
            this.op22Mux.x - convX(10),
            this.op22Mux.x - convX(10),
            this.op22Mux.x};
        int w42y[] = {this.wire[17].y[0], this.wire[17].y[0],
            this.shiftLeft.y + this.shiftLeft.height,
            this.wire[17].y[0], this.wire[17].y[0],
            this.op22Mux.y + this.op22Mux.height * 6 / 9,
            this.op22Mux.y + this.op22Mux.height * 6 / 9
        };
        wire[42] = new WireLabel(w42x, w42y);

        int w43x[] = {this.op22Mux.x - convX(10), this.aluControl.x};
        int w43y[] = {this.wire[17].y[0], this.wire[17].y[0]};
        wire[43] = new WireLabel(w43x, w43y);

        int w44x[] = {this.shiftLeft.x + this.shiftLeft.width,
            this.wire[33].x[1] + convX(13),
            this.wire[33].x[1] + convX(13),
            this.brunchAddX[0]};
        int w44y[] = {this.shiftLeft.y + this.shiftLeft.height / 2,
            this.shiftLeft.y + this.shiftLeft.height / 2,
            (this.brunchAddY[0] + this.brunchAddY[6]) / 2,
            (this.brunchAddY[0] + this.brunchAddY[6]) / 2};
        wire[44] = new WireLabel(w44x, w44y);

        int w45x[] = {this.op1Mux.x + this.op1Mux.width, this.aluX[0]};
        int w45y[] = {this.op1Mux.y + this.op1Mux.height / 2,
            this.op1Mux.y + this.op1Mux.height / 2};
        wire[45] = new WireLabel(w45x, w45y);

        int w46x[] = {this.op22Mux.x, this.op2Mux.x + this.op2Mux.width,
            this.op22Mux.x - convX(20), this.op22Mux.x - convX(20),
            this.ex_Mem.x};
        int w46y[] = {this.op22Mux.y + convY(20), this.op22Mux.y + convY(20),
            this.op22Mux.y + convY(20),
            this.op22Mux.y + this.op22Mux.height + convY(20),
            this.op22Mux.y + this.op22Mux.height + convY(20)};
        wire[46] = new WireLabel(w46x, w46y);

        int w47x[] = {this.op22Mux.x + this.op22Mux.width, this.aluX[0]};
        int w47y[] = {this.op22Mux.y + this.op22Mux.height / 2 - convY(3),
            this.op22Mux.y + this.op22Mux.height / 2 - convY(3)};
        wire[47] = new WireLabel(w47x, w47y);

        int w48x[] = {this.aluControl.x + this.aluControl.width / 2,
            this.aluControl.x + this.aluControl.width / 2};
        int w48y[] = {this.aluControl.y, (this.aluY[2] + this.aluY[3]) / 2};
        wire[48] = new WireLabel(w48x, w48y);

        int w49x[] = {this.id_Ex.x + this.id_Ex.width,
            this.id_Ex.x + this.id_Ex.width + convX(60),
            this.id_Ex.x + this.id_Ex.width + convX(60),
            this.fwdUnit.x};
        int w49y[] = {this.wire[18].y[0], this.wire[18].y[0],
            this.fwdUnit.y + this.fwdUnit.height * 3 / 5,
            this.fwdUnit.y + this.fwdUnit.height * 3 / 5};
        wire[49] = new WireLabel(w49x, w49y);

        int w50x[] = {this.id_Ex.x + this.id_Ex.width,
            this.id_Ex.x + this.id_Ex.width + convX(45),
            this.id_Ex.x + this.id_Ex.width + convX(45),
            this.fwdUnit.x};
        int w50y[] = {this.wire[19].y[0], this.wire[19].y[0],
            this.fwdUnit.y + this.fwdUnit.height * 4 / 5,
            this.fwdUnit.y + this.fwdUnit.height * 4 / 5};
        wire[50] = new WireLabel(w50x, w50y);

        int w51x[] = {this.id_Ex.x + this.id_Ex.width, this.desMux.x};
        int w51y[] = {this.wire[20].y[0], this.wire[20].y[0]};
        wire[51] = new WireLabel(w51x, w51y);

        int w52x[] = {this.id_Ex.x + this.id_Ex.width, this.desMux.x};
        int w52y[] = {this.wire[21].y[0], this.wire[21].y[0]};
        wire[52] = new WireLabel(w52x, w52y);

        int w53x[] = {this.desMux.x + this.desMux.width, this.ex_Mem.x};
        int w53y[] = {this.desMux.y + this.desMux.height / 2,
            this.desMux.y + this.desMux.height / 2};
        wire[53] = new WireLabel(w53x, w53y);

        int w54x[] = {this.aluX[1], this.ex_Mem.x};
        int w54y[] = {(this.aluY[1] + this.aluY[2]) / 2 - convY(10),
            (this.aluY[1] + this.aluY[2]) / 2 - convY(10)};
        wire[54] = new WireLabel(w54x, w54y);

        int w55x[] = {this.aluX[1], this.ex_Mem.x};
        int w55y[] = {(this.aluY[1] + this.aluY[2]) / 2,
            (this.aluY[1] + this.aluY[2]) / 2};
        wire[55] = new WireLabel(w55x, w55y);

        int w56x[] = {this.op1Mux.x + this.op1Mux.width / 2,
            this.op1Mux.x + this.op1Mux.width / 2,
            this.op1Mux.x + this.op1Mux.width / 2 + convX(65),
            this.op1Mux.x + this.op1Mux.width / 2 + convX(65),
            this.fwdUnit.x};

        int w56y[] = {this.op1Mux.y + this.op1Mux.height,
            this.op1Mux.y + this.op1Mux.height + convY(10),
            this.op1Mux.y + this.op1Mux.height + convY(10),
            this.fwdUnit.y + this.fwdUnit.height * 1 / 5,
            this.fwdUnit.y + this.fwdUnit.height * 1 / 5};
        wire[56] = new WireLabel(w56x, w56y);

        int w57x[] = {this.op2Mux.x + this.op2Mux.width / 2,
            this.op2Mux.x + this.op2Mux.width / 2,
            this.op2Mux.x + this.op2Mux.width / 2 + convX(50),
            this.op2Mux.x + this.op2Mux.width / 2 + convX(50),
            this.fwdUnit.x};
        int w57y[] = {this.op2Mux.y + this.op2Mux.height,
            this.op2Mux.y + this.op2Mux.height + convY(10),
            this.op2Mux.y + this.op2Mux.height + convY(10),
            this.fwdUnit.y + this.fwdUnit.height * 2 / 5,
            this.fwdUnit.y + this.fwdUnit.height * 2 / 5};
        wire[57] = new WireLabel(w57x, w57y);

        int w58x[] = {this.ex_Mem.x + this.ex_Mem.width, this.mem_Wb.x};
        int w58y[] = {this.ex_Mem.y + convY(15), this.ex_Mem.y + convY(15)};
        wire[58] = new WireLabel(w58x, w58y);

        int w59x[] = {this.ex_Mem.x + this.ex_Mem.width,
            this.memUnit.x + this.memUnit.width - convY(12),
            this.memUnit.x + this.memUnit.width - convY(12)};
        int w59y[] = {this.mem_Wb.y + convY(34), this.mem_Wb.y + convY(34),
            this.memUnit.y};
        wire[59] = new WireLabel(w59x, w59y);

        int w60x[] = {this.ex_Mem.x + this.ex_Mem.width,
            this.memUnit.x + this.memUnit.width - convY(24),
            this.memUnit.x + this.memUnit.width - convY(24)};
        int w60y[] = {this.mem_Wb.y + convY(43), this.mem_Wb.y + convY(43),
            this.memUnit.y};
        wire[60] = new WireLabel(w60x, w60y);

        int w61x[] = {this.ex_Mem.x + this.ex_Mem.width, this.andArc.x + convY(10),
            this.andArc.x + convY(10), this.andArc.x + this.andArc.width / 2};
        int w61y[] = {this.mem_Wb.y + convY(51), this.mem_Wb.y + convY(51),
            this.andArc.y + this.andArc.height * 1 / 3,
            this.andArc.y + this.andArc.height * 1 / 3};
        wire[61] = new WireLabel(w61x, w61y);

        int w62x[] = {this.ex_Mem.x + this.ex_Mem.width,
            this.ex_Mem.x + this.ex_Mem.width + convX(15),
            this.ex_Mem.x + this.ex_Mem.width + convX(15),
            this.andArc.x + this.andArc.width / 2};
        int w62y[] = {this.wire[54].y[0], this.wire[54].y[0],
            this.andArc.y + this.andArc.height * 2 / 3,
            this.andArc.y + this.andArc.height * 2 / 3};
        wire[62] = new WireLabel(w62x, w62y);

        int w63x[] = {this.ex_Mem.x + this.ex_Mem.width + convX(30),
            this.ex_Mem.x + this.ex_Mem.width + convX(30),
            this.fwdUnit.x + this.fwdUnit.width};
        int w63y[] = {this.wire[58].y[0],
            this.fwdUnit.y + this.fwdUnit.height * 3 / 6,
            this.fwdUnit.y + this.fwdUnit.height * 3 / 6};
        wire[63] = new WireLabel(w63x, w63y);

        int w64x[] = {this.ex_Mem.x + this.ex_Mem.width, this.memUnit.x};
        int w64y[] = {this.wire[55].y[0], this.wire[55].y[0]};
        wire[64] = new WireLabel(w64x, w64y);

        int w65x[] = {this.ex_Mem.x + this.ex_Mem.width, this.memUnit.x};
        int w65y[] = {this.wire[46].y[4], this.wire[46].y[4]};
        wire[65] = new WireLabel(w65x, w65y);

        int w66x[] = {this.ex_Mem.x + this.ex_Mem.width + convX(15),
            this.ex_Mem.x + this.ex_Mem.width + convX(15),
            this.mem_Wb.x};
        int w66y[] = {this.wire[55].y[0],
            this.memUnit.y + this.memUnit.height + convY(20),
            this.memUnit.y + this.memUnit.height + convY(20)};
        wire[66] = new WireLabel(w66x, w66y);

        int w67x[] = {this.ex_Mem.x + this.ex_Mem.width, this.mem_Wb.x};
        int w67y[] = {this.wire[53].y[0], this.wire[53].y[0]};
        wire[67] = new WireLabel(w67x, w67y);

        int w68x[] = {this.ex_Mem.x + this.ex_Mem.width + convX(15),
            this.ex_Mem.x + this.ex_Mem.width + convX(15),
            this.fwdUnit.x + this.fwdUnit.width};
        int w68y[] = {this.wire[53].y[0],
            this.fwdUnit.y + this.fwdUnit.height * 2 / 6,
            this.fwdUnit.y + this.fwdUnit.height * 2 / 6};
        wire[68] = new WireLabel(w68x, w68y);

        int w69x[] = {this.fwdUnit.x + this.fwdUnit.width, this.wire[15].x[4]};
        int w69y[] = {this.fwdUnit.y + this.fwdUnit.height * 4 / 6,
            this.fwdUnit.y + this.fwdUnit.height * 4 / 6};
        wire[69] = new WireLabel(w69x, w69y);

        int w70x[] = {this.hazUnit.x + convX(34), this.hazUnit.x + convX(34),
            this.wire[50].x[2]};
        int w70y[] = {this.hazUnit.y + this.hazUnit.height,
            this.fwdUnit.y + this.fwdUnit.height * 4 / 5,
            this.fwdUnit.y + this.fwdUnit.height * 4 / 5};
        wire[70] = new WireLabel(w70x, w70y);

        int w71x[] = {this.mem_Wb.x + this.mem_Wb.width,
            this.wbMux.x + this.wbMux.width / 2,
            this.wbMux.x + this.wbMux.width / 2};
        int w71y[] = {this.mem_Wb.y + convY(20), this.mem_Wb.y + convY(20),
            this.wbMux.y};
        wire[71] = new WireLabel(w71x, w71y);

        int w72x[] = {this.memUnit.x + this.memUnit.width, this.mem_Wb.x};
        int w72y[] = {this.wbMux.y + this.wbMux.height * 1 / 3 - convY(4),
            this.wbMux.y + this.wbMux.height * 1 / 3 - convY(4)};
        wire[72] = new WireLabel(w72x, w72y);

        int w73x[] = {this.mem_Wb.x + this.mem_Wb.width, this.wbMux.x};
        int w73y[] = {this.wbMux.y + this.wbMux.height * 1 / 3 - convY(4),
            this.wbMux.y + this.wbMux.height * 1 / 3 - convY(4)};
        wire[73] = new WireLabel(w73x, w73y);

        int w74x[] = {this.mem_Wb.x + this.mem_Wb.width,
            (this.mem_Wb.x + this.mem_Wb.width + this.wbMux.x) / 2,
            (this.mem_Wb.x + this.mem_Wb.width + this.wbMux.x) / 2,
            this.wbMux.x};
        int w74y[] = {this.wire[66].y[2], this.wire[66].y[2],
            this.wbMux.y + this.wbMux.height * 2 / 3 + convY(4),
            this.wbMux.y + this.wbMux.height * 2 / 3 + convY(4)};
        wire[74] = new WireLabel(w74x, w74y);

        int w75x[] = {this.hazUnit.x + this.hazUnit.width,
            this.id_Ex.x + this.id_Ex.width + convX(20),
            this.id_Ex.x + this.id_Ex.width + convX(20)};
        int w75y[] = {this.hazUnit.y + this.hazUnit.height * 2 / 4,
            this.hazUnit.y + this.hazUnit.height * 2 / 4,
            this.id_Ex.y + convY(42),};
        wire[75] = new WireLabel(w75x, w75y);

        /*
        int w75x[] = {};
        int w75y[] = { };
        wire[75] = new WireLabel(w75x,w75y);
         */
        for (int i = 0; i < this.wire.length; i++) {
            wire[i].setBounds(0, 0, this.width, this.height);
            wire[i].setOpaque(false);
            wire[i].setBackground(activeWColor);
            wire[i].color = Color.BLACK;
        }
    }

    public String getValueOfWire() {
        wire[this.tempLastWireSelect].color = this.normalWColor;
        wire[this.tempLastWireSelect].normalStroke();
        for (int i = 0; i < this.wire.length; i++) {
            for (int j = 0; j < this.wire[i].x.length - 1; j++) {

                int x1 = this.wire[i].x[j];
                int y1 = this.wire[i].y[j];

                int x2 = this.wire[i].x[j + 1];
                int y2 = this.wire[i].y[j + 1];
                if (Point.distance(x1, y1, x, y) + Point.distance(x, y, x2, y2)
                        - Point.distance(x1, y1, x2, y2) <= 0.1) {
                    //
                    this.tempLastWireSelect = i;
                    wire[i].color = this.activeWColor;
                    wire[i].hardStroke();

                    /* Toolkit t = Toolkit.getDefaultToolkit();
                    Image img = t.createImage("C:\\Users\\M. Tahmid Bari\\Desktop\\pencil.gif");
                    this.setCursor(t.createCustomCursor(img,new Point(0,0), TOOL_TIP_TEXT_KEY)); */
                    this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    String retStr = "";
                    switch (i + 1) {
                        case 1:
                            //if(this.Frames[m].hdUnit.checkBranchHazard())
                            //    retStr = "" + this.Frames[m].hdUnit.branchAddress;
                            //else if(this.Frames[m].ifIdPipeline!=null)
                            //    retStr = "" + this.Frames[m].ifIdPipeline.PC;
                            retStr = "" + this.Frames[m].nextPC;
                            if (mode == 2) {
                                retStr = giveBinaryString(0, 32, retStr); 
                            }else if (mode == 3) {
                                retStr = "Next PC";
                            }
                            break;
                        case 2:
                            retStr = "" + !this.Frames[m].hdUnit.checkDataHazard();
                            if (mode == 2) {
                                if (retStr.equalsIgnoreCase("true")) {
                                    retStr = "" + 1; 
                                }else {
                                    retStr = "" + 0;
                                }
                            } else if (mode == 3) {
                                retStr = "PC Write";
                            }
                            break;
                        case 3:
                            retStr = "" + this.Frames[m].currentPC;
                            if (mode == 2) {
                                retStr = giveBinaryString(0, 32, retStr); 
                            }else if (mode == 3) {
                                retStr = "Current PC";
                            }
                            break;
                        case 4:
                            retStr = "4";
                            if (mode == 2) {
                                retStr = giveBinaryString(0, 32, retStr); 
                            }else if (mode == 3) {
                                retStr = "PC Increament";
                            }
                            break;
                        case 5:
                            retStr = "" + (this.Frames[m].currentPC + 4);
                            if (mode == 2) {
                                retStr = giveBinaryString(0, 32, retStr); 
                            }else if (mode == 3) {
                                retStr = "PC + 4";
                            }
                            break;
                        case 6:
                            if (this.Frames[m].hdUnit.branchAddress != -1) {
                                retStr = "" + this.Frames[m].hdUnit.branchAddress;
                                if (mode == 2) {
                                    retStr = giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Branch Address";
                            }
                            break;
                        case 7:
                            retStr = "" + this.Frames[m].hdUnit.checkBranchHazard();
                            if (mode == 2) {
                                if (retStr.equalsIgnoreCase("true")) {
                                    retStr = "" + 1; 
                                }else {
                                    retStr = "" + 0;
                                }
                            } else if (mode == 3) {
                                retStr = "Branch";
                            }
                            break;
                        case 8:
                            if (this.Frames[m].ifIdPipeline != null) {
                                retStr = "" + this.Frames[m].ifIdPipeline.Instruction.getiString(); 
                            }else {
                                retStr = "X";
                            }
                            if (mode == 2) {
                                retStr = this.Frames[m].ifIdPipeline.Instruction.getBitString();
                            } else if (mode == 3) {
                                retStr = "32 bit Ins.";
                            }
                            break;
                        case 9:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.PC;
                                if (mode == 2) {
                                    retStr = giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "PC + 4";
                            }
                            break;
                        case 10:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.iString;
                                if (mode == 2) {
                                    retStr = this.Frames[m].idExPipeline.bitString;
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "32 bit Ins.";
                            }
                            break;
                        case 11:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].cUnit.op;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(26, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "OpCode";
                            }
                            break;
                        case 12:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.rt;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rt";
                            }
                            break;
                        case 13:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.rs;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rs";
                            }
                            break;
                        case 14:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.rt;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rt";
                            }
                            break;
                        case 15:
                            if (!this.Frames[m].wbiString.equalsIgnoreCase("NOP")) {
                                retStr = "" + this.Frames[m].fwdUnit.RegWriteMemWbValue;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Reg Write Value";
                            }
                            break;
                        case 16:
                            if (!this.Frames[m].wbiString.equalsIgnoreCase("NOP")) {
                                retStr = "" + this.Frames[m].fwdUnit.RegWriteMemWbAdd;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Reg Write Address";
                            }
                            break;
                        case 17:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.Offset;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(16, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Offset";
                            }
                            break;
                        case 18:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.Offset;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "SignEx 32 bit Offset";
                            }
                            break;
                        case 19:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.rs;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rs";
                            }
                            break;
                        case 20:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.rt;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rt";
                            }
                            break;
                        case 21:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.rt;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rt";
                            }
                            break;
                        case 22:
                            if (this.Frames[m].idExPipeline != null) {
                                if (this.Frames[m].idExPipeline.rd >= 0) {
                                    retStr = "" + this.Frames[m].idExPipeline.rd;
                                    if (mode == 2) {
                                        retStr = "" + giveBinaryString(27, 32, retStr);
                                    }
                                } else {
                                    retStr = this.Frames[m].idExPipeline.bitString.substring(16, 21);
                                    if (mode == 1) {
                                        retStr = "" + Integer.parseInt(retStr, 2);
                                    }
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rd";
                            }
                            break;
                        case 23:
                            retStr = "" + this.Frames[m].tempWbRegWrite;
                            if (mode == 2) {
                                if (retStr.equalsIgnoreCase("true")) {
                                    retStr = "" + 1; 
                                }else {
                                    retStr = "" + 0;
                                }
                            } else if (mode == 3) {
                                retStr = "Reg Write";
                            }
                            break;
                        case 24:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.WB.toString(mode); 
                            }else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "RegWrite, MemToReg";
                            }
                            break;
                        case 25:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.MEM.toString(mode); 
                            }else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "MemRead, MemWrite, Branch";
                            }
                            break;
                        case 26:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].cUnit.toString(mode); 
                            }else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "ALUOp, ALUSrc , RegDest";
                            }
                            break;
                        case 27:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.rsValue;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "Rs Value";
                            }
                            break;
                        case 28:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.rtValue;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "Rs Value";
                            }
                            break;
                        case 29:
                            if (this.Frames[m].idExPipeline != null) {
                                retStr = "" + this.Frames[m].idExPipeline.rs;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rs";
                            }
                            break;
                        case 30:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].exMemPipeline.MEM.toString(mode); 
                            }else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "MemRead, MemWrite, Branch";
                            }
                            break;
                        case 31:
                            retStr = "" + this.Frames[m].hdUnit.checkBranchHazard();
                            if (mode == 2) {
                                if (retStr.equalsIgnoreCase("true")) {
                                    retStr = "" + 1; 
                                }else {
                                    retStr = "" + 0;
                                }
                            } else if (mode == 3) {
                                retStr = "EX/MEM Flush";
                            }
                            break;
                        case 32:
                            retStr = "" + this.Frames[m].hdUnit.checkBranchHazard();
                            if (mode == 2) {
                                if (retStr.equalsIgnoreCase("true")) {
                                    retStr = "" + 1; 
                                }else {
                                    retStr = "" + 0;
                                }
                            } else if (mode == 3) {
                                retStr = "ID/EX Flush";
                            }
                            break;
                        case 33:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].exMemPipeline.WB.toString(mode); 
                            }else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "RegWrite, MemToReg";
                            }
                            break;
                        case 34:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].tempExAluOp;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(30, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "ALUOp";
                            }
                            break;
                        case 35:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].tempExDestReg;
                                if (mode == 2) {
                                    if (retStr.equalsIgnoreCase("true")) {
                                        retStr = "" + 1; 
                                    }else {
                                        retStr = "" + 0;
                                    }
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Dest Reg Select";
                            }
                            break;
                        case 36:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].tempExAluSource;
                                if (mode == 2) {
                                    if (retStr.equalsIgnoreCase("true")) {
                                        retStr = "" + 1; 
                                    }else {
                                        retStr = "" + 0;
                                    }
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "ALU Src Select";
                            }
                            break;
                        case 37:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].tempExPC;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "PC + 4";
                            }
                            break;
                        case 38:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].exMemPipeline.PCBranch;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Branch Address";
                            }
                            break;
                        case 39:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].fwdUnit.rsValue;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rs Value";
                            }
                            break;
                        case 40:
                            if (!this.Frames[m].wbiString.equalsIgnoreCase("NOP")) {
                                retStr = "" + this.Frames[m].fwdUnit.RegWriteMemWbValue;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Reg from MemWb";
                            }
                            break;
                        case 42:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].fwdUnit.rtValue;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rt Value";
                            }
                            break;
                        case 43:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].tempExI32Offset;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Offset";
                            }
                            break;
                        case 44:
                            if (this.Frames[m].exMemPipeline != null) {
                                if (this.Frames[m].tempExFunction >= 0) {
                                    retStr = "" + this.Frames[m].tempExFunction;
                                    if (mode == 2) {
                                        retStr = "" + giveBinaryString(26, 32, retStr);
                                    }
                                } else {
                                    retStr = "" + giveBinaryString(26, 32, "" + this.Frames[m].tempExI32Offset);
                                    if (mode == 1) {
                                        retStr = "" + Integer.parseInt(retStr, 2);
                                    }
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Function";
                            }
                            break;
                        case 45:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].tempExShiftLeft2Offset;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Shift Left 2 Offset";
                            }
                            break;
                        case 46:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].fwdUnit.valueMuxA();
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Operand 1";
                            }
                            break;
                        case 47:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].fwdUnit.valueMuxB();
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rt Value";
                            }
                            break;
                        case 48:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].tempExOperand2;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Operand 2";
                            }
                            break;
                        case 49:
                            if (mode == 3) {
                                retStr = "ALU Control";
                            }
                            break;
                        case 50:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].fwdUnit.rs;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rs";
                            }
                            break;
                        case 71:
                        case 51:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].fwdUnit.rt;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rt";
                            }
                            break;
                        case 52:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].fwdUnit.rt;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rt";
                            }
                            break;
                        case 53:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].tempExRd;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Rd";
                            }
                            break;
                        case 54:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].exMemPipeline.destReg;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Dest. Reg.";
                            }
                            break;
                        case 55:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].exMemPipeline.ALU_zero;
                                if (mode == 2) {
                                    if (retStr.equalsIgnoreCase("true")) {
                                        retStr = "" + 1; 
                                    }else {
                                        retStr = "" + 0;
                                    }
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "ALU Zero";
                            }
                            break;
                        case 56:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].exMemPipeline.ALU_result;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "ALU Result";
                            }
                            break;
                        case 57:
                            retStr = "" + this.Frames[m].fwdUnit.selectMuxA();
                            if (mode == 2) {
                                retStr = "" + giveBinaryString(30, 32, retStr);
                            }
                            if (mode == 3) {
                                retStr = "Fwd Mux 1 Select";
                            }
                            break;
                        case 58:
                            retStr = "" + this.Frames[m].fwdUnit.selectMuxB();
                            if (mode == 2) {
                                retStr = "" + giveBinaryString(30, 32, retStr);
                            }
                            if (mode == 3) {
                                retStr = "Fwd Mux 2 Select";
                            }
                            break;
                        case 59:
                            if (this.Frames[m].memRbPipeline != null) {
                                retStr = "" + this.Frames[m].memRbPipeline.WB.toString(mode); 
                            }else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "RegWrite, MemToReg";
                            }
                            break;
                        case 60:
                            retStr = "" + this.Frames[m].tempMemMRead;
                            if (mode == 2) {
                                if (retStr.equalsIgnoreCase("true")) {
                                    retStr = "" + 1; 
                                }else {
                                    retStr = "" + 0;
                                }
                            }
                            if (mode == 3) {
                                retStr = "Memory Read";
                            }
                            break;
                        case 61:
                            retStr = "" + this.Frames[m].tempMemMWrite;
                            if (mode == 2) {
                                if (retStr.equalsIgnoreCase("true")) {
                                    retStr = "" + 1; 
                                }else {
                                    retStr = "" + 0;
                                }
                            }
                            if (mode == 3) {
                                retStr = "Memory Write";
                            }
                            break;
                        case 62:
                            if (this.Frames[m].memRbPipeline != null) {
                                retStr = "" + this.Frames[m].hdUnit.branch;
                                if (mode == 2) {
                                    if (retStr.equalsIgnoreCase("true")) {
                                        retStr = "" + 1; 
                                    }else {
                                        retStr = "" + 0;
                                    }
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Branch";
                            }
                            break;
                        case 63:
                            if (this.Frames[m].memRbPipeline != null) {
                                retStr = "" + this.Frames[m].hdUnit.ALUzero;
                                if (mode == 2) {
                                    if (retStr.equalsIgnoreCase("true")) {
                                        retStr = "" + 1; 
                                    }else {
                                        retStr = "" + 0;
                                    }
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "ALU Zero";
                            }
                            break;
                        case 64:
                            if (this.Frames[m].memRbPipeline != null) {
                                retStr = "" + this.Frames[m].fwdUnit.RegWriteExMemFlag;
                                if (mode == 2) {
                                    if (retStr.equalsIgnoreCase("true")) {
                                        retStr = "" + 1; 
                                    }else {
                                        retStr = "" + 0;
                                    }
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Reg. Write";
                            }
                            break;
                        case 66:
                            if (this.Frames[m].memRbPipeline != null) {
                                retStr = "" + this.Frames[m].tempMemWriteData;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Memory Write Data";
                            }
                            break;
                        case 41:
                            if (mode == 3) {
                                retStr = "Reg from ExMem";
                                break;
                            }
                        case 65:
                        case 67:
                            if (this.Frames[m].memRbPipeline != null) {
                                retStr = "" + this.Frames[m].memRbPipeline.ALU_result;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "ALU Result/Mem Address";
                            }
                            break;
                        case 68:
                        case 69:
                            if (this.Frames[m].memRbPipeline != null) {
                                retStr = "" + this.Frames[m].memRbPipeline.destReg;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Dest. Reg.";
                            }
                            break;
                        case 70:
                            if (!this.Frames[m].wbiString.equalsIgnoreCase("NOP")) {
                                retStr = "" + this.Frames[m].fwdUnit.RegWriteMemWbAdd;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(27, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Reg. Write Address";
                            }
                            break;
                        case 72:
                            if (!this.Frames[m].wbiString.equalsIgnoreCase("NOP")) {
                                retStr = "" + this.Frames[m].tempWbMemToReg;
                                if (mode == 2) {
                                    if (retStr.equalsIgnoreCase("true")) {
                                        retStr = "" + 1; 
                                    }else {
                                        retStr = "" + 0;
                                    }
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Mem To Reg";
                            }
                            break;
                        case 73:
                            if (this.Frames[m].memRbPipeline != null) {
                                retStr = "" + this.Frames[m].memRbPipeline.Mdata;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Memory Data";
                            }
                            break;
                        case 74:
                            if (!this.Frames[m].wbiString.equalsIgnoreCase("NOP")) {
                                retStr = "" + this.Frames[m].tempWbMData;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Memory Data";
                            }
                            break;
                        case 75:
                            if (!this.Frames[m].wbiString.equalsIgnoreCase("NOP")) {
                                retStr = "" + this.Frames[m].tempWbAluResult;
                                if (mode == 2) {
                                    retStr = "" + giveBinaryString(0, 32, retStr);
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "ALU Result";
                            }
                            break;
                        case 76:
                            if (this.Frames[m].exMemPipeline != null) {
                                retStr = "" + this.Frames[m].exMemPipeline.MEM.MemRead;
                                if (mode == 2) {
                                    if (retStr.equalsIgnoreCase("true")) {
                                        retStr = "" + 1; 
                                    }else {
                                        retStr = "" + 0;
                                    }
                                }
                            } else {
                                retStr = "X";
                            }
                            if (mode == 3) {
                                retStr = "Memory Read";
                            }
                            break;
                    }

                    return retStr;//+ " (" + (i+1) +")";
                }
            }
        }

        if (x > regFile.x && x < regFile.x + regFile.width) {
            if (y > regFile.y && y < regFile.y + regFile.height) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        return " ";
    }

    public class MouseEventHandler extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            if (e.getSource() == nBtn) {
                if (m < Frames.length - 1) {
                    m++;
                    initializeWires();
                    repaint();
                }
            } else if (e.getSource() == pBtn) {
                if (m > 0) {
                    m--;
                    initializeWires();
                    repaint();
                }
            } else if (e.getX() > regFile.x && e.getX() < regFile.x + regFile.width) {
                if (e.getY() > regFile.y && e.getY() < regFile.y + regFile.height) {
                    String regOut = "Register: Value\n";
                    for (int i = 0; i < Frames[m].register.length; i++) {

                        if (Frames[m].regFlag[i] == true) {
                            regOut += Registers[i] + ": " + Frames[m].register[i] + "           \t"; 
                        }else {
                            regOut += Registers[i] + ": X           ";
                        }

                        if ((i + 1) % 4 == 0) {
                            regOut += "\n";
                        }
                    }
                    JOptionPane.showMessageDialog(null, regOut);
                }
            }
        }

        public void mouseMoved(MouseEvent e) {

            x = e.getX();
            y = e.getY();
            repaint();
        }
    }

    public String giveBinaryString(int l, int r, String str) {
        int num = Integer.parseInt(str);
        int lZeros = Integer.numberOfLeadingZeros(num);
        String s = "";
        for (int i = 0; i < lZeros; i++) {
            s += "0";
        }
        s += Integer.toBinaryString(num);
        s = s.substring(l, r);

        return s;
    }

    private void drawText(Graphics2D g2d) {
        Font font = new Font("Arial", Font.PLAIN, convX(14) < convY(14) ? convX(14) : convY(14));
        g2d.setFont(font);

        if (this.Frames[m].ifIdPipeline != null) {
            g2d.drawString(this.Frames[m].ifIdPipeline.iString, 20, 620); 
        }else {
            g2d.drawString("\tNOP", 20, 620);
        }

        if (this.Frames[m].idExPipeline != null) {
            g2d.drawString(this.Frames[m].idExPipeline.iString, 260, 620); 
        }else {
            g2d.drawString("\tNOP", 260, 620);
        }

        if (this.Frames[m].exMemPipeline != null) {
            g2d.drawString(this.Frames[m].exMemPipeline.iString, 500, 620); 
        }else {
            g2d.drawString("\tNOP", 540, 620);
        }

        if (this.Frames[m].memRbPipeline != null) {
            g2d.drawString(this.Frames[m].memRbPipeline.iString, 730, 620); 
        }else {
            g2d.drawString("\tNOP", 750, 620);
        }

        if (!this.Frames[m].wbiString.equalsIgnoreCase("NOP")) {
            g2d.drawString(this.Frames[m].wbiString, 900, 620); 
        }else {
            g2d.drawString("\tNOP", 930, 620);
        }

        font = new Font("Arial", Font.PLAIN, convX(20) < convY(20) ? convX(20) : convY(20));
        g2d.setFont(font);

        g2d.drawString("Clock Pulse: " + (m + 1), 20, 460);

        g2d.drawString("Mode: ", 20, 582);

    }

    public int convX(int X) {
        return (X * this.width) / 1024;
    }

    public int[] convX(int[] X, int j) {
        for (int i = 0; i < X.length; i++) {
            X[i] = convX(X[i] + j);
        }
        return X;
    }

    public int convY(int Y) {
        return (Y * this.height) / 648;
    }

    public int[] convY(int[] Y, int j) {
        for (int i = 0; i < Y.length; i++) {
            Y[i] = convY(Y[i]) + j;
        }
        return Y;
    }
    int aluX[] = {convX(656), convX(703), convX(703), convX(656),
        convX(656), convX(670), convX(656), convX(656)};
    int aluY[] = {convY(225), convY(250), convY(322), convY(345), convY(300),
        convY(286), convY(272), convY(225)};
    int pcAddX[] = {convX(103), convX(120), convX(120), convX(103), convX(103),
        convX(108), convX(103), convX(103)};
    int pcAddY[] = {convY(182), convY(193), convY(216), convY(227), convY(207),
        convY(204), convY(201), convY(182)};
    int brunchAddX[] = {convX(670), convX(683), convX(683), convX(670), convX(670),
        convX(674), convX(670), convX(670)};
    int brunchAddY[] = {convY(175), convY(185), convY(202), convY(211), convY(198),
        convY(194), convY(190), convY(175)};

    Rectangle PC = new Rectangle(convX(25), convY(306), convX(25), convY(75));
    Rectangle insMem = new Rectangle(convX(75), convY(267), convX(84), convY(140));
    Rectangle regFile = new Rectangle(convX(280), convY(220), convX(82), convY(110));
    Rectangle hazUnit = new Rectangle(convX(210), convY(35), convX(112), convY(52));
    Rectangle memUnit = new Rectangle(convX(787), convY(260), convX(82), convY(138));
    Rectangle fwdUnit = new Rectangle(convX(575), convY(520), convX(110), convY(52));
    Rectangle if_Id = new Rectangle(convX(180), convY(150), convX(24), convY(324));
    Rectangle id_Ex = new Rectangle(convX(390), convY(110), convX(24), convY(430));
    Rectangle ex_Mem = new Rectangle(convX(723), convY(110), convX(24), convY(420));
    Rectangle mem_Wb = new Rectangle(convX(890), convY(110), convX(24), convY(420));

    //Following are the ellipses implemented using Rectangle
    Rectangle cUnit = new Rectangle(convX(268), convY(110), convX(55), convY(80));
    Rectangle pcMux = new Rectangle(convX(100), convY(95), convX(37), convY(50));
    Rectangle op1Mux = new Rectangle(convX(460), convY(220), convX(42), convY(60));
    Rectangle op2Mux = new Rectangle(convX(460), convY(305), convX(42), convY(60));
    Rectangle wbMux = new Rectangle(convX(935), convY(290), convX(37), convY(50));
    Rectangle signEx = new Rectangle(convX(295), convY(397), convX(35), convY(70));
    Rectangle op22Mux = new Rectangle(convX(577), convY(308), convX(38), convY(45));
    Rectangle desMux = new Rectangle(convX(485), convY(490), convX(35), convY(45));
    Rectangle aluControl = new Rectangle(convX(665), convY(405), convX(34), convY(56));
    Rectangle shiftLeft = new Rectangle(convX(470), convY(385), convX(25), convY(40));
    Rectangle andArc = new Rectangle(convX(780), convY(187), convX(40), convY(28));
}
