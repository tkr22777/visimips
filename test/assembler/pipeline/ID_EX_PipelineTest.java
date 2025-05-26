package assembler.pipeline;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import assembler.instruction.Instruction;

/**
 * Tests for ID_EX_Pipeline with dependency injection support
 */
public class ID_EX_PipelineTest {

    private ID_EX_Pipeline pipeline;
    private WB mockWB;
    private MEM mockMEM;

    @Before
    public void setUp() {
        mockWB = new WB();
        mockMEM = new MEM();
    }

    @Test
    public void testDefaultConstructor() {
        pipeline = new ID_EX_Pipeline();
        assertNotNull("Pipeline should be created", pipeline);
        assertNotNull("WB should be initialized", pipeline.WB);
        assertNotNull("MEM should be initialized", pipeline.MEM);

        // Check default values
        assertFalse("RegDest should be false by default", pipeline.RegDest);
        assertFalse("ALUSrc should be false by default", pipeline.ALUSrc);
        assertEquals("ALUOp should be -1 by default", -1, pipeline.ALUOp);
        assertEquals("PC should be 0 by default", 0, pipeline.PC);
        assertEquals("rs should be -1 by default", -1, pipeline.rs);
        assertEquals("rt should be -1 by default", -1, pipeline.rt);
        assertEquals("rd should be -1 by default", -1, pipeline.rd);
        assertEquals("iString should be NOP by default", "NOP", pipeline.iString);
    }

    @Test
    public void testDependencyInjectionConstructor() {
        pipeline = new ID_EX_Pipeline(mockWB, mockMEM);
        assertNotNull("Pipeline should be created", pipeline);
        assertSame("WB should be injected dependency", mockWB, pipeline.WB);
        assertSame("MEM should be injected dependency", mockMEM, pipeline.MEM);
    }

    @Test
    public void testFieldAssignment() {
        pipeline = new ID_EX_Pipeline(mockWB, mockMEM);

        // Test setting control signals
        pipeline.RegDest = true;
        pipeline.ALUSrc = true;
        pipeline.ALUOp = 2;

        assertTrue("RegDest should be set", pipeline.RegDest);
        assertTrue("ALUSrc should be set", pipeline.ALUSrc);
        assertEquals("ALUOp should be set", 2, pipeline.ALUOp);
    }

    @Test
    public void testRegisterValues() {
        pipeline = new ID_EX_Pipeline(mockWB, mockMEM);

        // Test setting register values
        pipeline.rs = 1;
        pipeline.rsValue = 100;
        pipeline.rt = 2;
        pipeline.rtValue = 200;
        pipeline.rd = 3;

        assertEquals("rs should be set", 1, pipeline.rs);
        assertEquals("rsValue should be set", 100, pipeline.rsValue);
        assertEquals("rt should be set", 2, pipeline.rt);
        assertEquals("rtValue should be set", 200, pipeline.rtValue);
        assertEquals("rd should be set", 3, pipeline.rd);
    }

    @Test
    public void testOffsetValues() {
        pipeline = new ID_EX_Pipeline(mockWB, mockMEM);

        // Test setting offset values
        pipeline.Offset = 100;
        pipeline.i32Offset = 100;

        assertEquals("Offset should be set", 100, pipeline.Offset);
        assertEquals("i32Offset should be set", 100, pipeline.i32Offset);
    }

    @Test
    public void testInstructionInfo() {
        pipeline = new ID_EX_Pipeline(mockWB, mockMEM);

        // Test setting instruction information
        pipeline.PC = 1000;
        pipeline.function = 32; // ADD function
        pipeline.iString = "add $t0, $t1, $t2";
        pipeline.bitString = "00000001001010100100000000100000";

        assertEquals("PC should be set", 1000, pipeline.PC);
        assertEquals("function should be set", 32, pipeline.function);
        assertEquals("iString should be set", "add $t0, $t1, $t2", pipeline.iString);
        assertEquals("bitString should be set", "00000001001010100100000000100000", pipeline.bitString);
    }

    @Test
    public void testFlushOperation() {
        pipeline = new ID_EX_Pipeline(mockWB, mockMEM);

        // Set up some values first
        pipeline.RegDest = true;
        pipeline.ALUSrc = true;
        pipeline.ALUOp = 2;
        pipeline.WB.RegWrite = true;
        pipeline.WB.MemToReg = true;
        pipeline.MEM.MemRead = true;
        pipeline.MEM.MemWrite = true;
        pipeline.MEM.branch = true;
        pipeline.iString = "add $t0, $t1, $t2";

        // Flush the pipeline
        pipeline.Flush();

        // Verify all control signals are reset
        assertFalse("RegDest should be false after flush", pipeline.RegDest);
        assertFalse("ALUSrc should be false after flush", pipeline.ALUSrc);
        assertEquals("ALUOp should be -1 after flush", -1, pipeline.ALUOp);
        assertFalse("WB.RegWrite should be false after flush", pipeline.WB.RegWrite);
        assertFalse("WB.MemToReg should be false after flush", pipeline.WB.MemToReg);
        assertFalse("MEM.MemRead should be false after flush", pipeline.MEM.MemRead);
        assertFalse("MEM.MemWrite should be false after flush", pipeline.MEM.MemWrite);
        assertFalse("MEM.branch should be false after flush", pipeline.MEM.branch);
        assertEquals("iString should be NOP after flush", "NOP", pipeline.iString);
    }

    @Test
    public void testControlSignalPropagation() {
        // Test that control signals can be properly set from ControlUnit
        mockWB.RegWrite = true;
        mockWB.MemToReg = false;
        mockMEM.MemRead = false;
        mockMEM.MemWrite = false;
        mockMEM.branch = false;

        pipeline = new ID_EX_Pipeline(mockWB, mockMEM);
        pipeline.ALUOp = 2;
        pipeline.ALUSrc = false;
        pipeline.RegDest = true;

        // Verify control signals are properly set
        assertTrue("WB.RegWrite should be propagated", pipeline.WB.RegWrite);
        assertFalse("WB.MemToReg should be propagated", pipeline.WB.MemToReg);
        assertFalse("MEM.MemRead should be propagated", pipeline.MEM.MemRead);
        assertFalse("MEM.MemWrite should be propagated", pipeline.MEM.MemWrite);
        assertFalse("MEM.branch should be propagated", pipeline.MEM.branch);
        assertEquals("ALUOp should be set", 2, pipeline.ALUOp);
        assertFalse("ALUSrc should be set", pipeline.ALUSrc);
        assertTrue("RegDest should be set", pipeline.RegDest);
    }

    @Test
    public void testNegativeValues() {
        pipeline = new ID_EX_Pipeline(mockWB, mockMEM);

        // Test handling of negative values
        pipeline.rs = -1;
        pipeline.rt = -1;
        pipeline.rd = -1;
        pipeline.Offset = -100;
        pipeline.i32Offset = -100;
        pipeline.function = -1;

        assertEquals("Negative rs should be handled", -1, pipeline.rs);
        assertEquals("Negative rt should be handled", -1, pipeline.rt);
        assertEquals("Negative rd should be handled", -1, pipeline.rd);
        assertEquals("Negative Offset should be handled", -100, pipeline.Offset);
        assertEquals("Negative i32Offset should be handled", -100, pipeline.i32Offset);
        assertEquals("Negative function should be handled", -1, pipeline.function);
    }

    @Test
    public void testZeroValues() {
        pipeline = new ID_EX_Pipeline(mockWB, mockMEM);

        // Test handling of zero values
        pipeline.rs = 0;
        pipeline.rsValue = 0;
        pipeline.rt = 0;
        pipeline.rtValue = 0;
        pipeline.rd = 0;
        pipeline.PC = 0;

        assertEquals("Zero rs should be handled", 0, pipeline.rs);
        assertEquals("Zero rsValue should be handled", 0, pipeline.rsValue);
        assertEquals("Zero rt should be handled", 0, pipeline.rt);
        assertEquals("Zero rtValue should be handled", 0, pipeline.rtValue);
        assertEquals("Zero rd should be handled", 0, pipeline.rd);
        assertEquals("Zero PC should be handled", 0, pipeline.PC);
    }
}
