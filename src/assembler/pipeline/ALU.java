/**
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */
package assembler.pipeline;

import java.io.Serializable;

public class ALU implements Serializable {

    ALU() {
    }

    int process(int A, int B, int function, int ALUOp) {
        if (ALUOp == 0) {
            return A + B;
        } else if (ALUOp == 1) {
            return A - B;
        } else if (ALUOp == 2) {
            switch (function) {
                case 24:
                    return A * B;
                case 26:
                    return A / B;
                case 32:
                    return A + B;
                case 34:
                    return A - B;
                case 36:
                    return A & B;
                case 37:
                    return A | B;
                case 39:
                    return ~(A | B);
                case 42:
                    if (A < B) {
                        return 1;
                    } else {
                        return 0;
                    }
            }
        }
        return 0;
    }
}
