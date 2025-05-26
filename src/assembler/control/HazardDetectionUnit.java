package assembler.control;

import java.io.Serializable;

/**
 * @author Tahsin Kabir M Tahmid Bari
 */
public class HazardDetectionUnit implements Serializable {

    public boolean idExMemRead;
    public int idExRt;
    public int ifIdRs;
    public int ifIdRt;
    public char ifType;
    public boolean ALUzero;
    public boolean branch;
    public int branchAddress;

    public HazardDetectionUnit() {
        idExMemRead = false;
        idExRt = -10;
        ifIdRs = -11;
        ifIdRt = -12;
        ifType = 'N';
        ALUzero = false;
        branch = false;
        branchAddress = -1;
    }

    //will return true if hazard has occurd
    //Hazard can be reduced by checkhing the type of ins..there is something wrong with the logic    
    public boolean checkDataHazard() {
        if (idExMemRead) {
            if (ifType == 'R') {
                if (idExRt == ifIdRs || idExRt == ifIdRt) {
                    return true;
                } else {
                    return false;
                }
            } else if (ifType == 'I') {
                if (idExRt == ifIdRs) {
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

    public boolean checkBranchHazard() {
        if (ALUzero && branch) {
            return true;
        } else {
            return false;
        }
    }
}
