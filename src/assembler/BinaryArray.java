/**
 *
 * @author 'SIN
 */
package assembler;
public class BinaryArray {
    
    int[] bit;
    BinaryArray(int n)
    {
        bit = new int[n];
        for(int i = 0 ; i < n ; i ++)
        {
            bit[i] = 0;
        }
    }
    
    boolean setBit(int i , int j , String b)
    {
        if( (j-i+1 ) != b.length())
            return false;
        else
        {
            int k =  j - i ;            
            for(int l=0; l<=k ; l++ )
            {
                if(b.charAt(l) == '0')
                    bit[i+l] = 0;
                else if( b.charAt(l) == '1')
                    bit[i+l] = 1;
                else
                    return false;
            }
            return true;
        }
    }
    
    String getBit(int i, int j)
    {
        String b = "";
        while(i<=j)
        {
            b = b + bit[i];
            i++;
        }
        return b;
    }
    
    void printBinaryArray()
    {
        for(int i = bit.length - 1 ; i >=0 ; i ++)
        {
            System.out.print(bit[i]);
        }
    }
    
}
