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

    /**
     * Default constructor
     */
    public EX_MEM_Pipeline() {
        this(new WB(), new MEM());
    }

    /**
     * Constructor with dependency injection for better testability
     */
    public EX_MEM_Pipeline(WB wb, MEM mem) {
        this.WB = wb;
        this.MEM = mem;
        initializeDefaults();
    }

    /**
     * Initialize default values
     */
    private void initializeDefaults() {
        this.PCBranch = 0;
        this.ALU_result = 0;
        this.ALU_zero = false;
        this.MemDataWrite = 0;
        this.destReg = -1;
        this.iString = "NOP";
    }

    void Flush() {
        WB.MemToReg = false;
        WB.RegWrite = false;
        MEM.MemRead = false;
        MEM.MemWrite = false;
        MEM.branch = false;
        iString = "NOP";
    }
}
