
/**
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */
package assembler;
import java.io.Serializable;
public class EX_MEM_Pipeline implements Serializable{

    WB WB;
    MEM MEM;
    int PCBranch;
    int ALU_result;
    boolean ALU_zero;
    int MemDataWrite;
    int destReg;
    String iString;

    EX_MEM_Pipeline() {
    }

    void Flush() {
        this.WB.MemToReg = false;
        this.WB.RegWrite = false;
        this.MEM.MemRead = false;
        this.MEM.MemWrite = false;
        this.MEM.branch = false;
        this.iString = "NOP";
    }
}
