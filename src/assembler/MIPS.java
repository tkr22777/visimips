/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.util.Vector;
import assembler.instruction.Instruction;
import assembler.instruction.AssemblyCode;

/**
 *
 * @author 'SIN
 */
public class MIPS {

    Instruction[] instruction;
    String code;
    Vector Frames;
    MFrame FramesRet[];
    MFrame Frame;
    int i = 0;
    String[] Registers = {"$zero", "$at", "$v0", "$v1", "$a0", "$a1", "$a2",
        "$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6",
        "$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6",
        "$s7", "$t8", "$t9", "$k0", "$k1", "$gp", "$sp", "$fp",
        "$ra"};

    MIPS() {
        Frame = new MFrame();
        Frames = new Vector();
    }

    public MFrame[] execute(Instruction ins[]) {
        i = 0;
        int k = 0;
        while (i < ins.length + 5) {
            if (i < ins.length) {
                i = this.Frame.insertInstruction(ins[i], i * 4) / 4; 
            }else {
                this.Frame.insertInstruction(null, 0);
                i++;
            }
            /*    if(Frame.ifIdPipeline == null)
                System.out.println("IfId null at " + i);
            else if(Frame.ifIdPipeline.Instruction == null)
                System.out.println("IfId not null but ins null at " + i);
            else
               System.out.println("Instruction : " + Frame.ifIdPipeline.Instruction.iString + " | at " + i);
             */
            Frames.add(Frame.getCopy());
            k++;
        }

        for (int j = 0; j < 8; j++) {
            System.out.print(this.Registers[j] + ":\t" + this.Frame.register[j] + "\t");
            System.out.print(this.Registers[8 + j] + ":\t" + this.Frame.register[8 + j] + "\t");
            System.out.print(this.Registers[16 + j] + ":\t" + this.Frame.register[16 + j] + "\t");
            System.out.print(this.Registers[24 + j] + ":\t" + this.Frame.register[24 + j] + "\t");
            System.out.println();
        }
        i = 0;
        this.FramesRet = new MFrame[k];
        while (i < k) {
            FramesRet[i] = (MFrame) Frames.elementAt(i);
            if (FramesRet[i].ifIdPipeline == null) {
                System.out.println("IfId null at " + i); 
            }else if (FramesRet[i].ifIdPipeline.Instruction == null) {
                System.out.println("IfId not null but ins null at " + i); 
            }else {
                System.out.println("Instruction : " + FramesRet[i].ifIdPipeline.Instruction.iString + " | at " + i);
            }

            i++;
        }

        return FramesRet;
    }

    public MFrame[] execute(String str) {
        AssemblyCode asm = new AssemblyCode(str);
        Instruction[] ins;
        ins = asm.instructions;
        asm.printCode();
        return this.execute(ins);
    }

}
