/**
 *
 * @author 'SIN
 */
package assembler.pipeline;
import java.io.Serializable;
public class MEM implements Serializable {

    public boolean branch;
    public boolean MemRead;
    public boolean MemWrite;

    public String toString(int mode)
    {
        String str = "";
        if(mode==1){
            if(MemRead)
                str += "True, ";
            else
                str += "False, ";
            if(MemWrite)
                str += "True, ";
            else
                str += "False, ";
            if(branch)
                str += "True, ";
            else
                str += "False";
        }else if(mode==2){
            if(MemRead)
                str += "1, ";
            else
                str += "0, ";
            if(MemWrite)
                str += "1, ";
            else
                str += "0, ";
            if(branch)
                str += "1";
            else
                str += "0";
        }else if(mode==3)
            return "X";
        return str;

    }
}
