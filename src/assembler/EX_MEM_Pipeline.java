
/**
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */
package assembler;
import java.io.Serializable;
public class EX_MEM_Pipeline implements Serializable {

    WB WB;
    MEM MEM;
    int PCBranch;
    int ALU_result;
    boolean ALU_zero;
    int MemDataWrite;
    int destReg;
    String iString;

    void Flush() {
        WB.MemToReg = false;
        WB.RegWrite = false;
        MEM.MemRead = false;
        MEM.MemWrite = false;
        MEM.branch = false;
        iString = "NOP";
    }
}
