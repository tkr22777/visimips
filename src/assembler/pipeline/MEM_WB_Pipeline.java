/**
 *
 * @author 'SIN
 */
package assembler.pipeline;

import java.io.Serializable;

public class MEM_WB_Pipeline implements Serializable {

    public WB WB;
    public int ALU_result;
    public int Mdata;
    public int destReg;
    public String iString;

    /**
     * Default constructor
     */
    public MEM_WB_Pipeline() {
        this(new WB());
    }

    /**
     * Constructor with dependency injection for better testability
     */
    public MEM_WB_Pipeline(WB wb) {
        this.WB = wb;
        initializeDefaults();
    }

    /**
     * Initialize default values
     */
    private void initializeDefaults() {
        this.ALU_result = 0;
        this.Mdata = 0;
        this.destReg = -1;
        this.iString = "NOP";
    }
}
