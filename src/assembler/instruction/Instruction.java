/**
 *
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */
package assembler.instruction;

import java.io.Serializable;

public class Instruction implements Serializable {

    char type;
    int op;
    int rs;
    int rt;
    int rd;
    int shamt;
    int function;
    short offsetIJ;
    public String iString;

    public Instruction() {
        type = 'U';
        op = -1;
        rs = -1;
        rt = -1;
        rd = -1;
        shamt = -1;
        function = -1;
        offsetIJ = -1;
        this.iString = "UNKNOWN";
    }

    void printInstruction() {
        String msg = "Instruction Not Set";

        if (this.type == 'R') {
            msg = "Op :" + this.getOp() + " rs: " + this.getRs()
                    + " rt: " + this.getRt() + " rd: " + this.getRd()
                    + " \tshamt: " + this.getShamt() + " \tfunction: "
                    + this.getFunction();
        } else if (this.type == 'I') {
            msg = "Op :" + this.getOp() + " rs: " + this.getRs()
                    + " rt: " + this.getRt() + " \toffset/constant: "
                    + this.getOffsetIJ();
        } else if (this.type == 'J') {
            msg = "Op :" + this.getOp() + " JMP Address: " + this.getOffsetIJ();
        } else if (this.type == 'N') {
            msg = "NOP";
        }

        System.out.println(msg);
    }

    char getType() {
        return type;
    }

    void setType(char c) {
        this.type = c;
    }

    int getOp() {
        return op;
    }

    void setOp(int k) {
        if (k < 64 && k > -1) {
            this.op = k;
        } else {
            System.out.println("Op not Set");
        }
    }

    int getRs() {
        return rs;
    }

    void setRs(int k) {
        if (k < 32 && k > -1) {
            this.rs = k;
        } else {
            System.out.println("Rs not Set");
        }
    }

    int getRt() {
        return rt;
    }

    void setRt(int k) {

        if (k < 32 && k > -1) {
            this.rt = k;
        } else {
            System.out.println("Rt not Set");
        }

    }

    int getRd() {
        return rd;
    }

    void setRd(int k) {

        if (k < 32 && k > -1) {
            this.rd = k;
        } else {
            System.out.println("Rd not Set");
        }
    }

    int getShamt() {
        return shamt;
    }

    void setShamt(int k) {
        this.shamt = k;
    }

    int getFunction() {
        return function;
    }

    void setFunction(int k) {
        this.function = k;
    }

    short getOffsetIJ() {
        return offsetIJ;
    }

    void setOffsetIJ(short k) {
        this.offsetIJ = k;
    }

    String getiString() {
        return this.iString;
    }

    void setString(String s) {
        this.iString = s;
    }

    public String getBitString() {
        String str = "";
        if (this.type == 'R') {

            str = giveBinaryString(26, 32, "" + this.op);
            str += giveBinaryString(27, 32, "" + this.rs);
            str += giveBinaryString(27, 32, "" + this.rt);
            str += giveBinaryString(27, 32, "" + this.rd);
            str += giveBinaryString(27, 32, "" + this.shamt);
            str += giveBinaryString(26, 32, "" + this.function);

        } else if (this.type == 'I') {

            str = giveBinaryString(26, 32, "" + this.op);
            str += giveBinaryString(27, 32, "" + this.rs);
            str += giveBinaryString(27, 32, "" + this.rt);
            str += giveBinaryString(16, 32, "" + this.offsetIJ);

        } else {
            return "INVALID INSTRUCTION";
        }

        return str;
    }

    private String giveBinaryString(int l, int r, String str) {
        int num = Integer.parseInt(str);
        int lZeros = Integer.numberOfLeadingZeros(num);
        String s = "";
        for (int i = 0; i < lZeros; i++) {
            s += "0";
        }
        s += Integer.toBinaryString(num);
        s = s.substring(l, r);

        return s;
    }

}
