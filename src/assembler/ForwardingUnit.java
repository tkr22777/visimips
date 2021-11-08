/**
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */
package assembler;
import java.io.Serializable;

public class ForwardingUnit implements Serializable {

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
        rs = -2;
        rt = -2;

        RegWriteExMemAdd = -1;
        RegWriteMemWbValue = 0;
        RegWriteMemWbAdd = -1;
        RegWriteExMemValue = 0;
        RegWriteExMemFlag = false;
        RegWriteMemWbFlag = false;
    }

    int valueMuxA() {
        if (rs == RegWriteExMemAdd && RegWriteExMemFlag) {
            return RegWriteExMemValue;
        } else if (rs == RegWriteMemWbAdd && RegWriteMemWbFlag) {
            return RegWriteMemWbValue;
        } else {
            return rsValue;
        }
    }

    int valueMuxB() {
        if (rt == RegWriteExMemAdd && RegWriteExMemFlag) {
            return RegWriteExMemValue;
        } else if (rt == RegWriteMemWbAdd && RegWriteMemWbFlag) {
            return RegWriteMemWbValue;
        } else {
            return rtValue;
        }
    }
    int selectMuxA(){
        if (rs == RegWriteExMemAdd && RegWriteExMemFlag) {
            return 2;
        } else if (rs == RegWriteMemWbAdd && RegWriteMemWbFlag) {
            return 1;
        } else {
            return 0;
        }        
    }
    int selectMuxB() {

        if (rt == RegWriteExMemAdd && RegWriteExMemFlag) {
            return 2;
        } else if (rt == RegWriteMemWbAdd && RegWriteMemWbFlag) {
            return 1;
        } else {
            return 0;
        }
    }
}
