package assembler.pipeline;

import assembler.instruction.Instruction;
import java.io.Serializable;

/**
 *
 * @author Tahsin Kabir M Tahmid Bari
 */
public class IF_ID_Pipeline implements Serializable {

    public Instruction Instruction;
    public int PC;
    public String iString;

    /**
     * Default constructor for testing
     */
    public IF_ID_Pipeline() {
        this.Instruction = null;
        this.PC = 0;
        this.iString = "";
    }

    public IF_ID_Pipeline(Instruction i, int p) {
        this.Instruction = i;
        this.PC = p;
        this.iString = i.iString;
    }
}
