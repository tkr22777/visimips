/**
 *
 * @author 'SIN
 */
package assembler;

import java.io.Serializable;

public class MEM_WB_Pipeline implements Serializable {

    WB WB;
    int ALU_result;
    int Mdata;
    int destReg;
    String iString;

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
