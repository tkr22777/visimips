package assembler.pipeline;

import java.io.Serializable;

/**
 *
 * @author Tahsin Kabir M Tahmid Bari
 */
public class ID_EX_Pipeline implements Serializable {

    boolean RegDest;
    boolean ALUSrc;
    int ALUOp;
    MEM MEM;
    WB WB;
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

    /**
     * Default constructor
     */
    public ID_EX_Pipeline() {
        this(new WB(), new MEM());
    }

    /**
     * Constructor with dependency injection for better testability
     */
    public ID_EX_Pipeline(WB wb, MEM mem) {
        this.WB = wb;
        this.MEM = mem;
        initializeDefaults();
    }

    /**
     * Initialize default values
     */
    private void initializeDefaults() {
        this.RegDest = false;
        this.ALUSrc = false;
        this.ALUOp = -1;
        this.PC = 0;
        this.rs = -1;
        this.rsValue = 0;
        this.rt = -1;
        this.rtValue = 0;
        this.rd = -1;
        this.Offset = 0;
        this.i32Offset = 0;
        this.function = -1;
        this.iString = "NOP";
        this.bitString = "";
    }

    void Flush() {
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
