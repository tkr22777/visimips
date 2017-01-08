/**
 *
 * @author 'SIN
 */
package assembler;
import java.io.Serializable;
public class MEM_WB_Pipeline implements Serializable{
    
    WB  WB;
    int ALU_result;
    int Mdata;
    int destReg;
    String iString;
    
}
