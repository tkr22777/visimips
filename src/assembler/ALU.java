/**
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */
package assembler;
import java.io.Serializable;
public class ALU implements Serializable{
    
    int function;
    int ALUOp;    
    
    ALU()
    {
        this.function = -1;
        this.ALUOp = -1;
    }
    int process(int A,int B , int f , int al)
    {
        this.function = f;
        this.ALUOp = al;
                
        if(this.ALUOp == 0)
        {            
            return A + B;
        }
        
        if(this.ALUOp == 1)
            return A - B;
        
        if(this.ALUOp == 2)
        {
            if(this.function==24)
                return A*B;
            if(this.function==26)
                return A/B;
            if(this.function==32)
                return A+B;
            if(this.function==34)
                return A-B;
            if(this.function==36)
                return A&B;
            if(this.function==37)
                return A|B;
            if(this.function==39)
                return ~(A|B);
            if(this.function==42)
            {
                if(A<B)
                    return 1;
                else
                    return 0;
            }
        }
        return 0;
    }
}
