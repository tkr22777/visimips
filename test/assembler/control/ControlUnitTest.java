package assembler.control;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import assembler.pipeline.WB;
import assembler.pipeline.MEM;

/**
 * Tests for ControlUnit with dependency injection support
 */
public class ControlUnitTest {

    private ControlUnit controlUnit;
    private WB mockWB;
    private MEM mockMEM;

    @Before
    public void setUp() {
        mockWB = new WB();
        mockMEM = new MEM();
    }

    @Test
    public void testDefaultConstructor() {
        controlUnit = new ControlUnit(0);
        assertNotNull("ControlUnit should be created", controlUnit);
        assertNotNull("WB should be initialized", controlUnit.WB);
        assertNotNull("MEM should be initialized", controlUnit.MEM);
    }

    @Test
    public void testDependencyInjectionConstructor() {
        controlUnit = new ControlUnit(0, mockWB, mockMEM);
        assertNotNull("ControlUnit should be created", controlUnit);
        assertSame("WB should be injected dependency", mockWB, controlUnit.WB);
        assertSame("MEM should be injected dependency", mockMEM, controlUnit.MEM);
    }

    @Test
    public void testNOPInstruction() {
        controlUnit = new ControlUnit(-1, mockWB, mockMEM);
        assertEquals("Op should be -1 for NOP", -1, controlUnit.op);
        assertEquals("ALUOp should be -1 for NOP", -1, controlUnit.ALUOp);
        assertFalse("ALUSource should be false for NOP", controlUnit.ALUSource);
        assertFalse("RegDest should be false for NOP", controlUnit.RegDest);
    }

    @Test
    public void testRTypeInstruction() {
        controlUnit = new ControlUnit(0, mockWB, mockMEM);

        // Verify R-type control signals
        assertTrue("RegWrite should be true for R-type", controlUnit.WB.RegWrite);
        assertFalse("MemToReg should be false for R-type", controlUnit.WB.MemToReg);
        assertFalse("MemRead should be false for R-type", controlUnit.MEM.MemRead);
        assertFalse("MemWrite should be false for R-type", controlUnit.MEM.MemWrite);
        assertFalse("Branch should be false for R-type", controlUnit.MEM.branch);
        assertEquals("ALUOp should be 2 for R-type", 2, controlUnit.ALUOp);
        assertFalse("ALUSource should be false for R-type", controlUnit.ALUSource);
        assertTrue("RegDest should be true for R-type", controlUnit.RegDest);
    }

    @Test
    public void testBranchInstruction() {
        controlUnit = new ControlUnit(4, mockWB, mockMEM); // beq instruction

        // Verify branch control signals
        assertFalse("RegWrite should be false for branch", controlUnit.WB.RegWrite);
        assertFalse("MemToReg should be false for branch", controlUnit.WB.MemToReg);
        assertFalse("MemRead should be false for branch", controlUnit.MEM.MemRead);
        assertFalse("MemWrite should be false for branch", controlUnit.MEM.MemWrite);
        assertTrue("Branch should be true for branch", controlUnit.MEM.branch);
        assertEquals("ALUOp should be 1 for branch", 1, controlUnit.ALUOp);
        assertFalse("ALUSource should be false for branch", controlUnit.ALUSource);
        assertFalse("RegDest should be false for branch", controlUnit.RegDest);
    }

    @Test
    public void testLoadInstruction() {
        controlUnit = new ControlUnit(35, mockWB, mockMEM); // lw instruction

        // Verify load control signals
        assertTrue("RegWrite should be true for load", controlUnit.WB.RegWrite);
        assertTrue("MemToReg should be true for load", controlUnit.WB.MemToReg);
        assertTrue("MemRead should be true for load", controlUnit.MEM.MemRead);
        assertFalse("MemWrite should be false for load", controlUnit.MEM.MemWrite);
        assertFalse("Branch should be false for load", controlUnit.MEM.branch);
        assertEquals("ALUOp should be 0 for load", 0, controlUnit.ALUOp);
        assertTrue("ALUSource should be true for load", controlUnit.ALUSource);
        assertFalse("RegDest should be false for load", controlUnit.RegDest);
    }

    @Test
    public void testStoreInstruction() {
        controlUnit = new ControlUnit(43, mockWB, mockMEM); // sw instruction

        // Verify store control signals
        assertFalse("RegWrite should be false for store", controlUnit.WB.RegWrite);
        assertFalse("MemToReg should be false for store", controlUnit.WB.MemToReg);
        assertFalse("MemRead should be false for store", controlUnit.MEM.MemRead);
        assertTrue("MemWrite should be true for store", controlUnit.MEM.MemWrite);
        assertFalse("Branch should be false for store", controlUnit.MEM.branch);
        assertEquals("ALUOp should be 0 for store", 0, controlUnit.ALUOp);
        assertTrue("ALUSource should be true for store", controlUnit.ALUSource);
        assertFalse("RegDest should be false for store", controlUnit.RegDest);
    }

    @Test
    public void testAddImmediateInstruction() {
        controlUnit = new ControlUnit(8, mockWB, mockMEM); // addi instruction

        // Verify addi control signals
        assertTrue("RegWrite should be true for addi", controlUnit.WB.RegWrite);
        assertFalse("MemToReg should be false for addi", controlUnit.WB.MemToReg);
        assertFalse("MemRead should be false for addi", controlUnit.MEM.MemRead);
        assertFalse("MemWrite should be false for addi", controlUnit.MEM.MemWrite);
        assertFalse("Branch should be false for addi", controlUnit.MEM.branch);
        assertEquals("ALUOp should be 0 for addi", 0, controlUnit.ALUOp);
        assertTrue("ALUSource should be true for addi", controlUnit.ALUSource);
        assertFalse("RegDest should be false for addi", controlUnit.RegDest);
    }

    @Test
    public void testToStringMethods() {
        controlUnit = new ControlUnit(0, mockWB, mockMEM); // R-type

        // Test different toString modes
        String mode1 = controlUnit.toString(1);
        String mode2 = controlUnit.toString(2);
        String mode3 = controlUnit.toString(3);

        assertNotNull("toString mode 1 should not be null", mode1);
        assertNotNull("toString mode 2 should not be null", mode2);
        assertEquals("toString mode 3 should return empty string", "", mode3);
    }

    @Test
    public void testOpCodeStorage() {
        int[] testOpCodes = {-1, 0, 4, 8, 35, 43};

        for (int opCode : testOpCodes) {
            controlUnit = new ControlUnit(opCode, mockWB, mockMEM);
            assertEquals("OpCode should be stored correctly", opCode, controlUnit.op);
        }
    }
}
