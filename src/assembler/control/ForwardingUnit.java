/**
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */
package assembler.control;

import java.io.Serializable;

public class ForwardingUnit implements Serializable {

    public int rs;
    public int rt;
    public int RegWriteExMemAdd;
    public int RegWriteExMemValue;
    public int RegWriteMemWbAdd;
    public int RegWriteMemWbValue;
    public int rsValue;
    public int rtValue;
    public boolean RegWriteExMemFlag = false;
    public boolean RegWriteMemWbFlag = false;

    public ForwardingUnit() {
        rs = -2;
        rt = -2;

        RegWriteExMemAdd = -1;
        RegWriteMemWbValue = 0;
        RegWriteMemWbAdd = -1;
        RegWriteExMemValue = 0;
        RegWriteExMemFlag = false;
        RegWriteMemWbFlag = false;
    }

    public int valueMuxA() {
        if (rs == RegWriteExMemAdd && RegWriteExMemFlag) {
            return RegWriteExMemValue;
        } else if (rs == RegWriteMemWbAdd && RegWriteMemWbFlag) {
            return RegWriteMemWbValue;
        } else {
            return rsValue;
        }
    }

    public int valueMuxB() {
        if (rt == RegWriteExMemAdd && RegWriteExMemFlag) {
            return RegWriteExMemValue;
        } else if (rt == RegWriteMemWbAdd && RegWriteMemWbFlag) {
            return RegWriteMemWbValue;
        } else {
            return rtValue;
        }
    }

    public int selectMuxA() {
        if (rs == RegWriteExMemAdd && RegWriteExMemFlag) {
            return 2;
        } else if (rs == RegWriteMemWbAdd && RegWriteMemWbFlag) {
            return 1;
        } else {
            return 0;
        }
    }

    public int selectMuxB() {

        if (rt == RegWriteExMemAdd && RegWriteExMemFlag) {
            return 2;
        } else if (rt == RegWriteMemWbAdd && RegWriteMemWbFlag) {
            return 1;
        } else {
            return 0;
        }
    }
}
