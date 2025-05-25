package assembler;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class MEM_WB_PipelineTest {

    private MEM_WB_Pipeline pipeline;

    @Before
    public void setUp() {
        pipeline = new MEM_WB_Pipeline();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull("Pipeline object should be created", pipeline);
    }

    @Test
    public void testFieldAccess() {
        // Test that we can access and modify pipeline fields
        pipeline.ALU_result = 42;
        assertEquals("ALU result should be set", 42, pipeline.ALU_result);

        pipeline.Mdata = 100;
        assertEquals("Memory data should be set", 100, pipeline.Mdata);

        pipeline.destReg = 5;
        assertEquals("Destination register should be set", 5, pipeline.destReg);
    }

    @Test
    public void testWBControlSignals() {
        if (pipeline.WB != null) {
            pipeline.WB.RegWrite = true;
            pipeline.WB.MemToReg = false;
            assertTrue("RegWrite should be true", pipeline.WB.RegWrite);
            assertFalse("MemToReg should be false", pipeline.WB.MemToReg);
        }
    }
}
