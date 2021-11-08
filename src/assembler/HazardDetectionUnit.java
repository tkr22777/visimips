package assembler;
import java.io.Serializable;
/**
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */

public class HazardDetectionUnit implements Serializable {

    boolean idExMemRead;
    int idExRt;
    int ifIdRs;
    int ifIdRt;
    char ifType;
    boolean ALUzero;
    boolean branch;
    int branchAddress;

    HazardDetectionUnit() {
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
    boolean checkDataHazard() {
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

    boolean checkBranchHazard() {
        if (ALUzero && branch) {
            return true;
        } else {
            return false;
        }
    }
}
