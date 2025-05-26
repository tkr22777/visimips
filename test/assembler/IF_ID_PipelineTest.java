package assembler;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Tests for IF_ID_Pipeline register
 */
public class IF_ID_PipelineTest {

    private IF_ID_Pipeline pipeline;
    private Instruction mockInstruction;

    @Before
    public void setUp() {
        mockInstruction = new Instruction();
        mockInstruction.iString = "add $t0, $t1, $t2";
    }

    @Test
    public void testDefaultConstructor() {
        pipeline = new IF_ID_Pipeline();
        assertNotNull("Pipeline should be created", pipeline);
        assertNull("Instruction should be null by default", pipeline.Instruction);
        assertEquals("PC should be 0 by default", 0, pipeline.PC);
        assertEquals("iString should be empty by default", "", pipeline.iString);
    }

    @Test
    public void testParameterizedConstructor() {
        int testPC = 1000;
        pipeline = new IF_ID_Pipeline(mockInstruction, testPC);

        assertNotNull("Pipeline should be created", pipeline);
        assertSame("Instruction should be set", mockInstruction, pipeline.Instruction);
        assertEquals("PC should be set", testPC, pipeline.PC);
        assertEquals("iString should be copied from instruction", mockInstruction.iString, pipeline.iString);
    }

    @Test
    public void testInstructionAssignment() {
        pipeline = new IF_ID_Pipeline();
        pipeline.Instruction = mockInstruction;

        assertSame("Instruction should be assigned", mockInstruction, pipeline.Instruction);
    }

    @Test
    public void testPCAssignment() {
        pipeline = new IF_ID_Pipeline();

        // Test various PC values
        int[] testPCs = {0, 4, 8, 100, 1000, -4};
        for (int pc : testPCs) {
            pipeline.PC = pc;
            assertEquals("PC should be set to " + pc, pc, pipeline.PC);
        }
    }

    @Test
    public void testIStringAssignment() {
        pipeline = new IF_ID_Pipeline();

        String[] testStrings = {"", "nop", "add $t0, $t1, $t2", "lw $t0, 0($sp)"};
        for (String str : testStrings) {
            pipeline.iString = str;
            assertEquals("iString should be set", str, pipeline.iString);
        }
    }

    @Test
    public void testNullInstructionHandling() {
        // Test that null instruction causes expected behavior
        try {
            pipeline = new IF_ID_Pipeline(null, 100);
            fail("Should throw NullPointerException for null instruction");
        } catch (NullPointerException e) {
            // Expected behavior - the original code doesn't handle null gracefully
            assertTrue("Should throw NPE for null instruction", true);
        }
    }

    @Test
    public void testInstructionStringPropagation() {
        // Test that iString is properly copied from instruction
        String[] testStrings = {"add", "sub", "lw", "sw", "beq"};

        for (String str : testStrings) {
            Instruction inst = new Instruction();
            inst.iString = str;

            pipeline = new IF_ID_Pipeline(inst, 0);
            assertEquals("iString should match instruction", str, pipeline.iString);
        }
    }

    @Test
    public void testPipelineRegisterBehavior() {
        // Test typical pipeline register usage
        Instruction inst1 = new Instruction();
        inst1.iString = "add $t0, $t1, $t2";

        Instruction inst2 = new Instruction();
        inst2.iString = "sub $t3, $t4, $t5";

        // First instruction
        pipeline = new IF_ID_Pipeline(inst1, 1000);
        assertEquals("First instruction should be stored", inst1.iString, pipeline.iString);
        assertEquals("First PC should be stored", 1000, pipeline.PC);

        // Update to second instruction (simulating pipeline advance)
        pipeline.Instruction = inst2;
        pipeline.PC = 1004;
        pipeline.iString = inst2.iString;

        assertEquals("Second instruction should be stored", inst2.iString, pipeline.iString);
        assertEquals("Second PC should be stored", 1004, pipeline.PC);
    }

    @Test
    public void testSerializableInterface() {
        // Test that the class implements Serializable (compile-time check)
        pipeline = new IF_ID_Pipeline();
        assertTrue("Should implement Serializable", pipeline instanceof java.io.Serializable);
    }
}
