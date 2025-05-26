package assembler.pipeline;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Tests for WB (Write Back) stage control signals
 */
public class WBTest {

    private WB wb;

    @Before
    public void setUp() {
        wb = new WB();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull("WB object should be created", wb);
    }

    @Test
    public void testRegWriteFlag() {
        wb.RegWrite = true;
        assertTrue("RegWrite should be true after setting", wb.RegWrite);

        wb.RegWrite = false;
        assertFalse("RegWrite should be false after setting", wb.RegWrite);
    }

    @Test
    public void testMemToRegFlag() {
        wb.MemToReg = true;
        assertTrue("MemToReg should be true after setting", wb.MemToReg);

        wb.MemToReg = false;
        assertFalse("MemToReg should be false after setting", wb.MemToReg);
    }

    @Test
    public void testToStringMode1() {
        // Test all combinations for mode 1 (True/False format)
        wb.RegWrite = true;
        wb.MemToReg = true;
        assertEquals("Mode 1: both true", "True, True", wb.toString(1));

        wb.RegWrite = true;
        wb.MemToReg = false;
        assertEquals("Mode 1: RegWrite true, MemToReg false", "True, False", wb.toString(1));

        wb.RegWrite = false;
        wb.MemToReg = true;
        assertEquals("Mode 1: RegWrite false, MemToReg true", "False, True", wb.toString(1));

        wb.RegWrite = false;
        wb.MemToReg = false;
        assertEquals("Mode 1: both false", "False, False", wb.toString(1));
    }

    @Test
    public void testToStringMode2() {
        // Test all combinations for mode 2 (1/0 format)
        wb.RegWrite = true;
        wb.MemToReg = true;
        assertEquals("Mode 2: both true", "1, 1", wb.toString(2));

        wb.RegWrite = true;
        wb.MemToReg = false;
        assertEquals("Mode 2: RegWrite true, MemToReg false", "1, 0", wb.toString(2));

        wb.RegWrite = false;
        wb.MemToReg = true;
        assertEquals("Mode 2: RegWrite false, MemToReg true", "0, 1", wb.toString(2));

        wb.RegWrite = false;
        wb.MemToReg = false;
        assertEquals("Mode 2: both false", "0, 0", wb.toString(2));
    }

    @Test
    public void testToStringMode3() {
        // Mode 3 always returns "X" regardless of flag values
        wb.RegWrite = true;
        wb.MemToReg = true;
        assertEquals("Mode 3: should return X", "X", wb.toString(3));

        wb.RegWrite = false;
        wb.MemToReg = false;
        assertEquals("Mode 3: should return X regardless of flags", "X", wb.toString(3));
    }

    @Test
    public void testToStringInvalidMode() {
        // Test invalid modes (should return empty string based on code)
        wb.RegWrite = true;
        wb.MemToReg = true;
        assertEquals("Invalid mode 0", "", wb.toString(0));
        assertEquals("Invalid mode 4", "", wb.toString(4));
        assertEquals("Invalid mode -1", "", wb.toString(-1));
        assertEquals("Invalid mode 999", "", wb.toString(999));
    }

    @Test
    public void testSerializableInterface() {
        assertTrue("WB should implement Serializable", wb instanceof java.io.Serializable);
    }

}
