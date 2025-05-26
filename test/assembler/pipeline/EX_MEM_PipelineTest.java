package assembler.pipeline;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import assembler.instruction.Instruction;

/**
 * Tests for EX_MEM_Pipeline with dependency injection support
 */
public class EX_MEM_PipelineTest {

    private EX_MEM_Pipeline pipeline;
    private WB mockWB;
    private MEM mockMEM;

    @Before
    public void setUp() {
        mockWB = new WB();
        mockMEM = new MEM();
    }

    @Test
    public void testDefaultConstructor() {
        pipeline = new EX_MEM_Pipeline();
        assertNotNull("Pipeline should be created", pipeline);
        assertNotNull("WB should be initialized", pipeline.WB);
        assertNotNull("MEM should be initialized", pipeline.MEM);

        // Check default values
        assertEquals("PCBranch should be 0 by default", 0, pipeline.PCBranch);
        assertEquals("ALU_result should be 0 by default", 0, pipeline.ALU_result);
        assertFalse("ALU_zero should be false by default", pipeline.ALU_zero);
        assertEquals("MemDataWrite should be 0 by default", 0, pipeline.MemDataWrite);
        assertEquals("destReg should be -1 by default", -1, pipeline.destReg);
        assertEquals("iString should be NOP by default", "NOP", pipeline.iString);
    }

    @Test
    public void testDependencyInjectionConstructor() {
        pipeline = new EX_MEM_Pipeline(mockWB, mockMEM);
        assertNotNull("Pipeline should be created", pipeline);
        assertSame("WB should be injected dependency", mockWB, pipeline.WB);
        assertSame("MEM should be injected dependency", mockMEM, pipeline.MEM);
    }

    @Test
    public void testFieldAssignment() {
        pipeline = new EX_MEM_Pipeline(mockWB, mockMEM);

        // Test setting pipeline fields
        pipeline.PCBranch = 2000;
        pipeline.ALU_result = 42;
        pipeline.ALU_zero = true;
        pipeline.MemDataWrite = 100;
        pipeline.destReg = 5;
        pipeline.iString = "add $t0, $t1, $t2";

        assertEquals("PCBranch should be set", 2000, pipeline.PCBranch);
        assertEquals("ALU_result should be set", 42, pipeline.ALU_result);
        assertTrue("ALU_zero should be set", pipeline.ALU_zero);
        assertEquals("MemDataWrite should be set", 100, pipeline.MemDataWrite);
        assertEquals("destReg should be set", 5, pipeline.destReg);
        assertEquals("iString should be set", "add $t0, $t1, $t2", pipeline.iString);
    }

    @Test
    public void testControlSignalPropagation() {
        // Set up control signals in mock objects
        mockWB.RegWrite = true;
        mockWB.MemToReg = false;
        mockMEM.MemRead = false;
        mockMEM.MemWrite = true;
        mockMEM.branch = false;

        pipeline = new EX_MEM_Pipeline(mockWB, mockMEM);

        // Verify control signals are properly propagated
        assertTrue("WB.RegWrite should be propagated", pipeline.WB.RegWrite);
        assertFalse("WB.MemToReg should be propagated", pipeline.WB.MemToReg);
        assertFalse("MEM.MemRead should be propagated", pipeline.MEM.MemRead);
        assertTrue("MEM.MemWrite should be propagated", pipeline.MEM.MemWrite);
        assertFalse("MEM.branch should be propagated", pipeline.MEM.branch);
    }

    @Test
    public void testFlushOperation() {
        pipeline = new EX_MEM_Pipeline(mockWB, mockMEM);

        // Set up some values first
        pipeline.WB.RegWrite = true;
        pipeline.WB.MemToReg = true;
        pipeline.MEM.MemRead = true;
        pipeline.MEM.MemWrite = true;
        pipeline.MEM.branch = true;
        pipeline.iString = "add $t0, $t1, $t2";

        // Flush the pipeline
        pipeline.Flush();

        // Verify all control signals are reset
        assertFalse("WB.RegWrite should be false after flush", pipeline.WB.RegWrite);
        assertFalse("WB.MemToReg should be false after flush", pipeline.WB.MemToReg);
        assertFalse("MEM.MemRead should be false after flush", pipeline.MEM.MemRead);
        assertFalse("MEM.MemWrite should be false after flush", pipeline.MEM.MemWrite);
        assertFalse("MEM.branch should be false after flush", pipeline.MEM.branch);
        assertEquals("iString should be NOP after flush", "NOP", pipeline.iString);
    }

    @Test
    public void testALUResultHandling() {
        pipeline = new EX_MEM_Pipeline(mockWB, mockMEM);

        // Test various ALU results
        int[] testResults = {0, 1, -1, 42, -42, Integer.MAX_VALUE, Integer.MIN_VALUE};
        for (int result : testResults) {
            pipeline.ALU_result = result;
            assertEquals("ALU result should be " + result, result, pipeline.ALU_result);
        }
    }

    @Test
    public void testALUZeroFlag() {
        pipeline = new EX_MEM_Pipeline(mockWB, mockMEM);

        // Test ALU zero flag
        pipeline.ALU_zero = true;
        assertTrue("ALU_zero should be true", pipeline.ALU_zero);

        pipeline.ALU_zero = false;
        assertFalse("ALU_zero should be false", pipeline.ALU_zero);
    }

    @Test
    public void testBranchAddressHandling() {
        pipeline = new EX_MEM_Pipeline(mockWB, mockMEM);

        // Test branch address values
        int[] testAddresses = {0, 4, 8, 1000, 2000, -4};
        for (int addr : testAddresses) {
            pipeline.PCBranch = addr;
            assertEquals("PCBranch should be " + addr, addr, pipeline.PCBranch);
        }
    }

    @Test
    public void testMemoryDataHandling() {
        pipeline = new EX_MEM_Pipeline(mockWB, mockMEM);

        // Test memory data values
        int[] testData = {0, 1, -1, 255, -255, 0xFFFF, 0x12345678};
        for (int data : testData) {
            pipeline.MemDataWrite = data;
            assertEquals("MemDataWrite should be " + data, data, pipeline.MemDataWrite);
        }
    }

    @Test
    public void testDestinationRegisterHandling() {
        pipeline = new EX_MEM_Pipeline(mockWB, mockMEM);

        // Test destination register values (0-31 for MIPS, -1 for none)
        int[] testRegs = {-1, 0, 1, 15, 31};
        for (int reg : testRegs) {
            pipeline.destReg = reg;
            assertEquals("destReg should be " + reg, reg, pipeline.destReg);
        }
    }

    @Test
    public void testInstructionStringHandling() {
        pipeline = new EX_MEM_Pipeline(mockWB, mockMEM);

        String[] testStrings = {"NOP", "add $t0, $t1, $t2", "lw $t0, 0($sp)", "sw $t1, 4($sp)", "beq $t0, $t1, label"};
        for (String str : testStrings) {
            pipeline.iString = str;
            assertEquals("iString should be set", str, pipeline.iString);
        }
    }

    @Test
    public void testSerializableInterface() {
        pipeline = new EX_MEM_Pipeline();
        assertTrue("Should implement Serializable", pipeline instanceof java.io.Serializable);
    }
}
