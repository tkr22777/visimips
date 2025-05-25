package assembler;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class MEMTest {

    private MEM mem;

    @Before
    public void setUp() {
        mem = new MEM();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull("MEM object should be created", mem);
    }

    @Test
    public void testMemReadFlag() {
        mem.MemRead = true;
        assertTrue("MemRead should be true", mem.MemRead);

        mem.MemRead = false;
        assertFalse("MemRead should be false", mem.MemRead);
    }

    @Test
    public void testMemWriteFlag() {
        mem.MemWrite = true;
        assertTrue("MemWrite should be true", mem.MemWrite);

        mem.MemWrite = false;
        assertFalse("MemWrite should be false", mem.MemWrite);
    }

    @Test
    public void testBranchFlag() {
        mem.branch = true;
        assertTrue("branch should be true", mem.branch);

        mem.branch = false;
        assertFalse("branch should be false", mem.branch);
    }

    @Test
    public void testToStringModes() {
        mem.MemRead = true;
        mem.MemWrite = false;
        mem.branch = true;
        assertNotNull("toString mode 1", mem.toString(1));
        assertNotNull("toString mode 2", mem.toString(2));
        assertNotNull("toString mode 3", mem.toString(3));
    }
}
