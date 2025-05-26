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
    public void testToStringMode1() {
        // Test all combinations for mode 1 (True/False format)
        mem.MemRead = true;
        mem.MemWrite = true;
        mem.branch = true;
        assertEquals("Mode 1: all true", "True, True, True, ", mem.toString(1));

        mem.MemRead = true;
        mem.MemWrite = false;
        mem.branch = false;
        assertEquals("Mode 1: only MemRead true", "True, False, False", mem.toString(1));

        mem.MemRead = false;
        mem.MemWrite = true;
        mem.branch = false;
        assertEquals("Mode 1: only MemWrite true", "False, True, False", mem.toString(1));

        mem.MemRead = false;
        mem.MemWrite = false;
        mem.branch = true;
        assertEquals("Mode 1: only branch true", "False, False, True, ", mem.toString(1));

        mem.MemRead = false;
        mem.MemWrite = false;
        mem.branch = false;
        assertEquals("Mode 1: all false", "False, False, False", mem.toString(1));
    }

    @Test
    public void testToStringMode2() {
        // Test all combinations for mode 2 (1/0 format)
        mem.MemRead = true;
        mem.MemWrite = true;
        mem.branch = true;
        assertEquals("Mode 2: all true", "1, 1, 1", mem.toString(2));

        mem.MemRead = true;
        mem.MemWrite = false;
        mem.branch = false;
        assertEquals("Mode 2: only MemRead true", "1, 0, 0", mem.toString(2));

        mem.MemRead = false;
        mem.MemWrite = true;
        mem.branch = false;
        assertEquals("Mode 2: only MemWrite true", "0, 1, 0", mem.toString(2));

        mem.MemRead = false;
        mem.MemWrite = false;
        mem.branch = true;
        assertEquals("Mode 2: only branch true", "0, 0, 1", mem.toString(2));

        mem.MemRead = false;
        mem.MemWrite = false;
        mem.branch = false;
        assertEquals("Mode 2: all false", "0, 0, 0", mem.toString(2));
    }

    @Test
    public void testToStringMode3() {
        // Mode 3 always returns "X" regardless of flag values
        mem.MemRead = true;
        mem.MemWrite = true;
        mem.branch = true;
        assertEquals("Mode 3: should return X", "X", mem.toString(3));

        mem.MemRead = false;
        mem.MemWrite = false;
        mem.branch = false;
        assertEquals("Mode 3: should return X regardless of flags", "X", mem.toString(3));
    }

    @Test
    public void testToStringInvalidMode() {
        // Test invalid modes (should return empty string based on code)
        mem.MemRead = true;
        mem.MemWrite = true;
        mem.branch = true;
        assertEquals("Invalid mode 0", "", mem.toString(0));
        assertEquals("Invalid mode 4", "", mem.toString(4));
        assertEquals("Invalid mode -1", "", mem.toString(-1));
        assertEquals("Invalid mode 999", "", mem.toString(999));
    }

    @Test
    public void testSerializableInterface() {
        assertTrue("MEM should implement Serializable", mem instanceof java.io.Serializable);
    }

    @Test
    public void testMemoryOperationConflict() {
        // Test that both MemRead and MemWrite can be true (though unusual)
        mem.MemRead = true;
        mem.MemWrite = true;
        mem.branch = false;
        assertEquals("Both memory flags true should be allowed", "True, True, False", mem.toString(1));
    }
}
