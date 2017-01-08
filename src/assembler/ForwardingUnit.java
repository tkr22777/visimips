
/**
 *
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */
package assembler;
import java.io.Serializable;
public class ForwardingUnit implements Serializable{

    int rs;
    int rt;
    int RegWriteExMemAdd;
    int RegWriteExMemValue;
    int RegWriteMemWbAdd;
    int RegWriteMemWbValue;
    int rsValue;
    int rtValue;
    boolean RegWriteExMemFlag = false;
    boolean RegWriteMemWbFlag = false;

    ForwardingUnit() {
        this.rs = -2;
        this.rt = -2;

        this.RegWriteExMemAdd = -1;
        this.RegWriteMemWbValue = 0;
        this.RegWriteMemWbAdd = -1;
        this.RegWriteExMemValue = 0;
        this.RegWriteExMemFlag = false;
        this.RegWriteMemWbFlag = false;
    }
    int valueMuxA() {

        if (this.rs == this.RegWriteExMemAdd && this.RegWriteExMemFlag) {
            return this.RegWriteExMemValue;
        } else if (this.rs == this.RegWriteMemWbAdd && this.RegWriteMemWbFlag) {
            return this.RegWriteMemWbValue;
        } else {
            return this.rsValue;
        }
    }
    int valueMuxB() {

        if (this.rt == this.RegWriteExMemAdd && this.RegWriteExMemFlag) {
            return this.RegWriteExMemValue;
        } else if (this.rt == this.RegWriteMemWbAdd && this.RegWriteMemWbFlag) {
            return this.RegWriteMemWbValue;
        } else {
            return this.rtValue;
        }
    }
    int selectMuxA(){
        if (this.rs == this.RegWriteExMemAdd && this.RegWriteExMemFlag) {
            return 2;
        } else if (this.rs == this.RegWriteMemWbAdd && this.RegWriteMemWbFlag) {
            return 1;
        } else {
            return 0;
        }        
    }
    int selectMuxB() {

        if (this.rt == this.RegWriteExMemAdd && this.RegWriteExMemFlag) {
            return 2;
        } else if (this.rt == this.RegWriteMemWbAdd && this.RegWriteMemWbFlag) {
            return 1;
        } else {
            return 0;
        }
    }
}
