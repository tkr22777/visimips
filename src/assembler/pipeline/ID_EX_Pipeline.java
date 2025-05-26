package assembler.pipeline;

import java.io.Serializable;

/**
 *
 * @author Tahsin Kabir M Tahmid Bari
 */
public class ID_EX_Pipeline implements Serializable {

    public boolean RegDest;
    public boolean ALUSrc;
    public int ALUOp;
    public MEM MEM;
    public WB WB;
    public int PC;
    public int rs;
    public int rsValue;
    public int rt;
    public int rtValue;
    public int rd;
    public short Offset;
    public int i32Offset;
    public int function;
    public String iString;
    public String bitString;

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

    public void Flush() {
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
