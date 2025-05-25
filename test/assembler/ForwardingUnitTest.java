package assembler;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Tests for ForwardingUnit data forwarding logic
 */
public class ForwardingUnitTest {

    private ForwardingUnit forwardingUnit;

    @Before
    public void setUp() {
        forwardingUnit = new ForwardingUnit();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull("ForwardingUnit should be created", forwardingUnit);
        assertEquals("rs should be initialized to -2", -2, forwardingUnit.rs);
        assertEquals("rt should be initialized to -2", -2, forwardingUnit.rt);
        assertEquals("RegWriteExMemAdd should be -1", -1, forwardingUnit.RegWriteExMemAdd);
        assertEquals("RegWriteMemWbAdd should be -1", -1, forwardingUnit.RegWriteMemWbAdd);
        assertFalse("RegWriteExMemFlag should be false", forwardingUnit.RegWriteExMemFlag);
        assertFalse("RegWriteMemWbFlag should be false", forwardingUnit.RegWriteMemWbFlag);
    }

    @Test
    public void testNoForwardingMuxA() {
        // Setup: no forwarding needed
        forwardingUnit.rs = 1;
        forwardingUnit.rsValue = 100;
        forwardingUnit.RegWriteExMemAdd = 2; // Different register
        forwardingUnit.RegWriteMemWbAdd = 3; // Different register

        int result = forwardingUnit.valueMuxA();
        assertEquals("Should return original rs value when no forwarding", 100, result);
        assertEquals("Select should be 0 for no forwarding", 0, forwardingUnit.selectMuxA());
    }

    @Test
    public void testExMemForwardingMuxA() {
        // Setup: EX/MEM forwarding
        forwardingUnit.rs = 1;
        forwardingUnit.rsValue = 100;
        forwardingUnit.RegWriteExMemAdd = 1; // Same register
        forwardingUnit.RegWriteExMemValue = 200;
        forwardingUnit.RegWriteExMemFlag = true;

        int result = forwardingUnit.valueMuxA();
        assertEquals("Should return EX/MEM forwarded value", 200, result);
        assertEquals("Select should be 2 for EX/MEM forwarding", 2, forwardingUnit.selectMuxA());
    }

    @Test
    public void testMemWbForwardingMuxA() {
        // Setup: MEM/WB forwarding (when EX/MEM not available)
        forwardingUnit.rs = 1;
        forwardingUnit.rsValue = 100;
        forwardingUnit.RegWriteExMemAdd = 2; // Different register
        forwardingUnit.RegWriteMemWbAdd = 1; // Same register
        forwardingUnit.RegWriteMemWbValue = 300;
        forwardingUnit.RegWriteMemWbFlag = true;

        int result = forwardingUnit.valueMuxA();
        assertEquals("Should return MEM/WB forwarded value", 300, result);
        assertEquals("Select should be 1 for MEM/WB forwarding", 1, forwardingUnit.selectMuxA());
    }

    @Test
    public void testExMemPriorityOverMemWbMuxA() {
        // Setup: Both EX/MEM and MEM/WB available, EX/MEM should have priority
        forwardingUnit.rs = 1;
        forwardingUnit.rsValue = 100;
        forwardingUnit.RegWriteExMemAdd = 1;
        forwardingUnit.RegWriteExMemValue = 200;
        forwardingUnit.RegWriteExMemFlag = true;
        forwardingUnit.RegWriteMemWbAdd = 1;
        forwardingUnit.RegWriteMemWbValue = 300;
        forwardingUnit.RegWriteMemWbFlag = true;

        int result = forwardingUnit.valueMuxA();
        assertEquals("EX/MEM should have priority over MEM/WB", 200, result);
        assertEquals("Select should be 2 for EX/MEM priority", 2, forwardingUnit.selectMuxA());
    }

    @Test
    public void testNoForwardingMuxB() {
        // Setup: no forwarding needed
        forwardingUnit.rt = 2;
        forwardingUnit.rtValue = 150;
        forwardingUnit.RegWriteExMemAdd = 1; // Different register
        forwardingUnit.RegWriteMemWbAdd = 3; // Different register

        int result = forwardingUnit.valueMuxB();
        assertEquals("Should return original rt value when no forwarding", 150, result);
        assertEquals("Select should be 0 for no forwarding", 0, forwardingUnit.selectMuxB());
    }

    @Test
    public void testExMemForwardingMuxB() {
        // Setup: EX/MEM forwarding
        forwardingUnit.rt = 2;
        forwardingUnit.rtValue = 150;
        forwardingUnit.RegWriteExMemAdd = 2; // Same register
        forwardingUnit.RegWriteExMemValue = 250;
        forwardingUnit.RegWriteExMemFlag = true;

        int result = forwardingUnit.valueMuxB();
        assertEquals("Should return EX/MEM forwarded value", 250, result);
        assertEquals("Select should be 2 for EX/MEM forwarding", 2, forwardingUnit.selectMuxB());
    }

    @Test
    public void testMemWbForwardingMuxB() {
        // Setup: MEM/WB forwarding
        forwardingUnit.rt = 2;
        forwardingUnit.rtValue = 150;
        forwardingUnit.RegWriteExMemAdd = 1; // Different register
        forwardingUnit.RegWriteMemWbAdd = 2; // Same register
        forwardingUnit.RegWriteMemWbValue = 350;
        forwardingUnit.RegWriteMemWbFlag = true;

        int result = forwardingUnit.valueMuxB();
        assertEquals("Should return MEM/WB forwarded value", 350, result);
        assertEquals("Select should be 1 for MEM/WB forwarding", 1, forwardingUnit.selectMuxB());
    }

    @Test
    public void testForwardingDisabledWhenFlagsFalse() {
        // Setup: registers match but flags are false
        forwardingUnit.rs = 1;
        forwardingUnit.rt = 2;
        forwardingUnit.rsValue = 100;
        forwardingUnit.rtValue = 150;
        forwardingUnit.RegWriteExMemAdd = 1;
        forwardingUnit.RegWriteMemWbAdd = 2;
        forwardingUnit.RegWriteExMemFlag = false; // Disabled
        forwardingUnit.RegWriteMemWbFlag = false; // Disabled

        assertEquals("MuxA should return original value when flags false", 100, forwardingUnit.valueMuxA());
        assertEquals("MuxB should return original value when flags false", 150, forwardingUnit.valueMuxB());
        assertEquals("SelectA should be 0 when flags false", 0, forwardingUnit.selectMuxA());
        assertEquals("SelectB should be 0 when flags false", 0, forwardingUnit.selectMuxB());
    }

    @Test
    public void testBothMuxesIndependent() {
        // Setup: different forwarding for each mux
        forwardingUnit.rs = 1;
        forwardingUnit.rt = 2;
        forwardingUnit.rsValue = 100;
        forwardingUnit.rtValue = 150;

        // MuxA gets EX/MEM forwarding
        forwardingUnit.RegWriteExMemAdd = 1;
        forwardingUnit.RegWriteExMemValue = 200;
        forwardingUnit.RegWriteExMemFlag = true;

        // MuxB gets MEM/WB forwarding
        forwardingUnit.RegWriteMemWbAdd = 2;
        forwardingUnit.RegWriteMemWbValue = 300;
        forwardingUnit.RegWriteMemWbFlag = true;

        assertEquals("MuxA should get EX/MEM forwarding", 200, forwardingUnit.valueMuxA());
        assertEquals("MuxB should get MEM/WB forwarding", 300, forwardingUnit.valueMuxB());
        assertEquals("SelectA should be 2", 2, forwardingUnit.selectMuxA());
        assertEquals("SelectB should be 1", 1, forwardingUnit.selectMuxB());
    }

    @Test
    public void testZeroRegisterHandling() {
        // Test with register 0 (should still forward if conditions met)
        forwardingUnit.rs = 0;
        forwardingUnit.rt = 0;
        forwardingUnit.rsValue = 0;
        forwardingUnit.rtValue = 0;
        forwardingUnit.RegWriteExMemAdd = 0;
        forwardingUnit.RegWriteExMemValue = 42;
        forwardingUnit.RegWriteExMemFlag = true;

        assertEquals("Should forward even for register 0", 42, forwardingUnit.valueMuxA());
        assertEquals("Should forward even for register 0", 42, forwardingUnit.valueMuxB());
    }
}
