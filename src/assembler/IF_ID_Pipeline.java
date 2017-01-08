
package assembler;

import java.io.Serializable;
/**
 *
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */
public class IF_ID_Pipeline implements Serializable {

    Instruction Instruction;
    int PC;
    String iString;
    
    IF_ID_Pipeline(Instruction i, int p)
    {
        this.Instruction = i;
        this.PC = p;
        this.iString = i.iString;
    }
}
