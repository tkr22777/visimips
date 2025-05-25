package assembler;

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
    public void testToStringModes() {
        wb.RegWrite = true;
        wb.MemToReg = false;
        assertNotNull("toString mode 1", wb.toString(1));
        assertNotNull("toString mode 2", wb.toString(2));
        assertNotNull("toString mode 3", wb.toString(3));
    }

}
