package assembler.pipeline;

import java.io.Serializable;
import assembler.instruction.Instruction;

/**
 *
 * @author Tahsin Kabir M Tahmid Bari
 */
public class IF_ID_Pipeline implements Serializable {

    Instruction Instruction;
    int PC;
    String iString;

    /**
     * Default constructor for testing
     */
    public IF_ID_Pipeline() {
        this.Instruction = null;
        this.PC = 0;
        this.iString = "";
    }

    IF_ID_Pipeline(Instruction i, int p) {
        this.Instruction = i;
        this.PC = p;
        this.iString = i.iString;
    }
}
