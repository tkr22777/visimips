
package assembler;

import java.io.Serializable;
/**
 *
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */
public class ID_EX_Pipeline implements Serializable {
    
    boolean RegDest;
    boolean ALUSrc;
    int ALUOp;
    MEM MEM;
    WB  WB;
    int PC;
    int rs;
    int rsValue;
    int rt;
    int rtValue;
    int rd;
    short Offset;
    int i32Offset;
    int function;
    String iString;
    String bitString;
        
    void Flush()
    {
        this.ALUSrc = false;
        this.RegDest = false;
        this.WB.MemToReg = false;
        this.WB.RegWrite = false;
        this.MEM.MemRead = false;
        this.MEM.MemWrite = false;
        this.MEM.branch = false;
        this.ALUOp = -1;
        this.iString = "NOP";
    }
}
