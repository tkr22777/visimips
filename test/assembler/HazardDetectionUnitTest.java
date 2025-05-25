package assembler;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Tests for HazardDetectionUnit hazard detection logic
 */
public class HazardDetectionUnitTest {

    private HazardDetectionUnit hazardUnit;

    @Before
    public void setUp() {
        hazardUnit = new HazardDetectionUnit();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull("HazardDetectionUnit should be created", hazardUnit);
        assertFalse("idExMemRead should be false", hazardUnit.idExMemRead);
        assertEquals("idExRt should be -10", -10, hazardUnit.idExRt);
        assertEquals("ifIdRs should be -11", -11, hazardUnit.ifIdRs);
        assertEquals("ifIdRt should be -12", -12, hazardUnit.ifIdRt);
        assertEquals("ifType should be 'N'", 'N', hazardUnit.ifType);
        assertFalse("ALUzero should be false", hazardUnit.ALUzero);
        assertFalse("branch should be false", hazardUnit.branch);
        assertEquals("branchAddress should be -1", -1, hazardUnit.branchAddress);
    }

    @Test
    public void testNoDataHazardWhenMemReadFalse() {
        // Setup: MemRead is false, so no hazard regardless of registers
        hazardUnit.idExMemRead = false;
        hazardUnit.idExRt = 1;
        hazardUnit.ifIdRs = 1; // Same register
        hazardUnit.ifIdRt = 1; // Same register
        hazardUnit.ifType = 'R';

        assertFalse("No hazard when MemRead is false", hazardUnit.checkDataHazard());
    }

    @Test
    public void testRTypeDataHazardWithRs() {
        // Setup: R-type instruction with hazard on rs register
        hazardUnit.idExMemRead = true;
        hazardUnit.idExRt = 1;
        hazardUnit.ifIdRs = 1; // Hazard: same as idExRt
        hazardUnit.ifIdRt = 2; // Different register
        hazardUnit.ifType = 'R';

        assertTrue("Should detect hazard on rs register", hazardUnit.checkDataHazard());
    }

    @Test
    public void testRTypeDataHazardWithRt() {
        // Setup: R-type instruction with hazard on rt register
        hazardUnit.idExMemRead = true;
        hazardUnit.idExRt = 2;
        hazardUnit.ifIdRs = 1; // Different register
        hazardUnit.ifIdRt = 2; // Hazard: same as idExRt
        hazardUnit.ifType = 'R';

        assertTrue("Should detect hazard on rt register", hazardUnit.checkDataHazard());
    }

    @Test
    public void testRTypeDataHazardWithBothRegisters() {
        // Setup: R-type instruction with hazard on both registers
        hazardUnit.idExMemRead = true;
        hazardUnit.idExRt = 1;
        hazardUnit.ifIdRs = 1; // Hazard: same as idExRt
        hazardUnit.ifIdRt = 1; // Hazard: same as idExRt
        hazardUnit.ifType = 'R';

        assertTrue("Should detect hazard when both registers match", hazardUnit.checkDataHazard());
    }

    @Test
    public void testRTypeNoDataHazard() {
        // Setup: R-type instruction with no hazard
        hazardUnit.idExMemRead = true;
        hazardUnit.idExRt = 1;
        hazardUnit.ifIdRs = 2; // Different register
        hazardUnit.ifIdRt = 3; // Different register
        hazardUnit.ifType = 'R';

        assertFalse("Should not detect hazard when registers are different", hazardUnit.checkDataHazard());
    }

    @Test
    public void testITypeDataHazardWithRs() {
        // Setup: I-type instruction with hazard on rs register
        hazardUnit.idExMemRead = true;
        hazardUnit.idExRt = 1;
        hazardUnit.ifIdRs = 1; // Hazard: same as idExRt
        hazardUnit.ifIdRt = 2; // rt doesn't matter for I-type
        hazardUnit.ifType = 'I';

        assertTrue("Should detect hazard on rs register for I-type", hazardUnit.checkDataHazard());
    }

    @Test
    public void testITypeNoDataHazardWithRt() {
        // Setup: I-type instruction - rt register doesn't cause hazard
        hazardUnit.idExMemRead = true;
        hazardUnit.idExRt = 2;
        hazardUnit.ifIdRs = 1; // Different register
        hazardUnit.ifIdRt = 2; // Same as idExRt but shouldn't matter for I-type
        hazardUnit.ifType = 'I';

        assertFalse("Should not detect hazard on rt register for I-type", hazardUnit.checkDataHazard());
    }

    @Test
    public void testITypeNoDataHazard() {
        // Setup: I-type instruction with no hazard
        hazardUnit.idExMemRead = true;
        hazardUnit.idExRt = 1;
        hazardUnit.ifIdRs = 2; // Different register
        hazardUnit.ifIdRt = 3; // Doesn't matter for I-type
        hazardUnit.ifType = 'I';

        assertFalse("Should not detect hazard when rs is different for I-type", hazardUnit.checkDataHazard());
    }

    @Test
    public void testOtherInstructionTypesNoHazard() {
        // Setup: Other instruction types (J, N, etc.) should not cause hazards
        char[] types = {'J', 'N', 'U'};

        for (char type : types) {
            hazardUnit.idExMemRead = true;
            hazardUnit.idExRt = 1;
            hazardUnit.ifIdRs = 1; // Same register
            hazardUnit.ifIdRt = 1; // Same register
            hazardUnit.ifType = type;

            assertFalse("Type " + type + " should not cause data hazard", hazardUnit.checkDataHazard());
        }
    }

    @Test
    public void testNoBranchHazardWhenConditionsFalse() {
        // Test all combinations where branch hazard should not occur
        boolean[] aluZeroValues = {false, true};
        boolean[] branchValues = {false, true};

        for (boolean aluZero : aluZeroValues) {
            for (boolean branch : branchValues) {
                if (aluZero && branch) {
                    continue; // Skip the true case
                }
                hazardUnit.ALUzero = aluZero;
                hazardUnit.branch = branch;

                assertFalse("No branch hazard when ALUzero=" + aluZero + " and branch=" + branch,
                        hazardUnit.checkBranchHazard());
            }
        }
    }

    @Test
    public void testBranchHazardWhenBothTrue() {
        // Setup: Branch hazard occurs when both ALUzero and branch are true
        hazardUnit.ALUzero = true;
        hazardUnit.branch = true;

        assertTrue("Should detect branch hazard when both conditions are true", hazardUnit.checkBranchHazard());
    }

    @Test
    public void testBranchAddressHandling() {
        // Test that branch address can be set and retrieved
        hazardUnit.branchAddress = 100;
        assertEquals("Branch address should be stored correctly", 100, hazardUnit.branchAddress);

        hazardUnit.branchAddress = 0;
        assertEquals("Branch address should handle zero", 0, hazardUnit.branchAddress);

        hazardUnit.branchAddress = -1;
        assertEquals("Branch address should handle negative values", -1, hazardUnit.branchAddress);
    }

    @Test
    public void testComplexHazardScenario() {
        // Test a complex scenario with both data and branch hazards

        // First, set up a data hazard
        hazardUnit.idExMemRead = true;
        hazardUnit.idExRt = 1;
        hazardUnit.ifIdRs = 1;
        hazardUnit.ifType = 'R';

        // Also set up a branch hazard
        hazardUnit.ALUzero = true;
        hazardUnit.branch = true;

        assertTrue("Should detect data hazard", hazardUnit.checkDataHazard());
        assertTrue("Should detect branch hazard", hazardUnit.checkBranchHazard());
    }

    @Test
    public void testZeroRegisterHandling() {
        // Test hazard detection with register 0
        hazardUnit.idExMemRead = true;
        hazardUnit.idExRt = 0;
        hazardUnit.ifIdRs = 0;
        hazardUnit.ifIdRt = 0;
        hazardUnit.ifType = 'R';

        assertTrue("Should detect hazard even with register 0", hazardUnit.checkDataHazard());
    }
}
