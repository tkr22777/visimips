package assembler;
import java.io.Serializable;

/**
 *
 * @author 'SIN
 */
public class WB implements Serializable {
    
    boolean MemToReg;
    boolean RegWrite;  
    
    public String toString(int mode)
    {
        String str = "";
        if(mode==1){
            if(RegWrite)
                str += "True, ";
            else
                str += "False, ";

            if(MemToReg)
                str += "True";
            else
                str += "False";
        }else if(mode==2){
            if(RegWrite)
                str += "1, ";
            else
                str += "0, ";
            if(MemToReg)
                str += "1";
            else
                str += "0";
        }else if(mode==3)
            return "X";
        return str;

    }

}
