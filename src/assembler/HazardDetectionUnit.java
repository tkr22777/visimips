
package assembler;

import java.io.Serializable;

/**
 *
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */
public class HazardDetectionUnit implements Serializable{

    boolean idExMemRead;
    int idExRt;
    int ifIdRs;
    int ifIdRt;
    char ifType;
    boolean ALUzero;
    boolean branch;
    int branchAddress;

    HazardDetectionUnit() {
        this.idExMemRead = false;
        this.idExRt = -10;
        this.ifIdRs = -11;
        this.ifIdRt = -12;
        this.ifType = 'N';
        this.ALUzero = false;
        this.branch = false;
        this.branchAddress = -1;
    }

    //will return true if hazard has occurd
    //Hazard can be reduced by checkhing the type of ins..there is something wrong with the logic    
    boolean checkDataHazard() {
        if (this.idExMemRead) {
            if (this.ifType == 'R') {
                if (this.idExRt == this.ifIdRs || this.idExRt == this.ifIdRt) {
                    return true;
                } else {
                    return false;
                }
            } else if (this.ifType == 'I') {
                if (this.idExRt == this.ifIdRs) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    boolean checkBranchHazard() {
        if (this.ALUzero && this.branch) {
            return true;
        } else {
            return false;
        }
    }
}
