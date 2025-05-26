package assembler.instruction;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Tests for Instruction class
 */
public class InstructionTest {

    private Instruction instruction;

    @Before
    public void setUp() {
        instruction = new Instruction();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull("Instruction should be created", instruction);
        assertEquals("Type should be 'U' by default", 'U', instruction.getType());
        assertEquals("Op should be -1 by default", -1, instruction.getOp());
        assertEquals("Rs should be -1 by default", -1, instruction.getRs());
        assertEquals("Rt should be -1 by default", -1, instruction.getRt());
        assertEquals("Rd should be -1 by default", -1, instruction.getRd());
        assertEquals("Shamt should be -1 by default", -1, instruction.getShamt());
        assertEquals("Function should be -1 by default", -1, instruction.getFunction());
        assertEquals("OffsetIJ should be -1 by default", -1, instruction.getOffsetIJ());
        assertEquals("iString should be UNKNOWN by default", "UNKNOWN", instruction.getiString());
    }

    @Test
    public void testTypeSetterGetter() {
        char[] testTypes = {'R', 'I', 'J', 'N', 'U'};
        for (char type : testTypes) {
            instruction.setType(type);
            assertEquals("Type should be set to " + type, type, instruction.getType());
        }
    }

    @Test
    public void testOpSetterGetter() {
        // Test valid op codes (0-63)
        int[] validOps = {0, 1, 32, 35, 43, 63};
        for (int op : validOps) {
            instruction.setOp(op);
            assertEquals("Op should be set to " + op, op, instruction.getOp());
        }
    }

    @Test
    public void testOpInvalidValues() {
        // Test invalid op codes (should not be set)
        int originalOp = instruction.getOp();
        instruction.setOp(-1);
        assertEquals("Op should not change for -1", originalOp, instruction.getOp());

        instruction.setOp(64);
        assertEquals("Op should not change for 64", originalOp, instruction.getOp());
    }

    @Test
    public void testRsSetterGetter() {
        // Test valid register values (0-31)
        int[] validRegs = {0, 1, 15, 31};
        for (int reg : validRegs) {
            instruction.setRs(reg);
            assertEquals("Rs should be set to " + reg, reg, instruction.getRs());
        }
    }

    @Test
    public void testRsInvalidValues() {
        int originalRs = instruction.getRs();
        instruction.setRs(-1);
        assertEquals("Rs should not change for -1", originalRs, instruction.getRs());

        instruction.setRs(32);
        assertEquals("Rs should not change for 32", originalRs, instruction.getRs());
    }

    @Test
    public void testRtSetterGetter() {
        // Test valid register values (0-31)
        int[] validRegs = {0, 1, 15, 31};
        for (int reg : validRegs) {
            instruction.setRt(reg);
            assertEquals("Rt should be set to " + reg, reg, instruction.getRt());
        }
    }

    @Test
    public void testRtInvalidValues() {
        int originalRt = instruction.getRt();
        instruction.setRt(-1);
        assertEquals("Rt should not change for -1", originalRt, instruction.getRt());

        instruction.setRt(32);
        assertEquals("Rt should not change for 32", originalRt, instruction.getRt());
    }

    @Test
    public void testRdSetterGetter() {
        // Test valid register values (0-31)
        int[] validRegs = {0, 1, 15, 31};
        for (int reg : validRegs) {
            instruction.setRd(reg);
            assertEquals("Rd should be set to " + reg, reg, instruction.getRd());
        }
    }

    @Test
    public void testRdInvalidValues() {
        int originalRd = instruction.getRd();
        instruction.setRd(-1);
        assertEquals("Rd should not change for -1", originalRd, instruction.getRd());

        instruction.setRd(32);
        assertEquals("Rd should not change for 32", originalRd, instruction.getRd());
    }

    @Test
    public void testShamtSetterGetter() {
        // Test shift amount values
        int[] testShifts = {0, 1, 15, 31, -1};
        for (int shift : testShifts) {
            instruction.setShamt(shift);
            assertEquals("Shamt should be set to " + shift, shift, instruction.getShamt());
        }
    }

    @Test
    public void testFunctionSetterGetter() {
        // Test function codes
        int[] testFunctions = {0, 32, 34, 36, 37, 42, -1};
        for (int func : testFunctions) {
            instruction.setFunction(func);
            assertEquals("Function should be set to " + func, func, instruction.getFunction());
        }
    }

    @Test
    public void testOffsetIJSetterGetter() {
        // Test offset values
        short[] testOffsets = {0, 1, -1, 100, -100, Short.MAX_VALUE, Short.MIN_VALUE};
        for (short offset : testOffsets) {
            instruction.setOffsetIJ(offset);
            assertEquals("OffsetIJ should be set to " + offset, offset, instruction.getOffsetIJ());
        }
    }

    @Test
    public void testStringSetterGetter() {
        String[] testStrings = {"", "add", "add $t0, $t1, $t2", "lw $t0, 0($sp)", "NOP"};
        for (String str : testStrings) {
            instruction.setString(str);
            assertEquals("iString should be set to " + str, str, instruction.getiString());
        }
    }

    @Test
    public void testRTypeInstruction() {
        // Set up R-type instruction (add $t0, $t1, $t2)
        instruction.setType('R');
        instruction.setOp(0);
        instruction.setRs(9);  // $t1
        instruction.setRt(10); // $t2
        instruction.setRd(8);  // $t0
        instruction.setShamt(0);
        instruction.setFunction(32); // ADD function

        assertEquals("Type should be R", 'R', instruction.getType());
        assertEquals("Op should be 0", 0, instruction.getOp());
        assertEquals("Rs should be 9", 9, instruction.getRs());
        assertEquals("Rt should be 10", 10, instruction.getRt());
        assertEquals("Rd should be 8", 8, instruction.getRd());
        assertEquals("Function should be 32", 32, instruction.getFunction());
    }

    @Test
    public void testITypeInstruction() {
        // Set up I-type instruction (lw $t0, 100($sp))
        instruction.setType('I');
        instruction.setOp(35); // LW
        instruction.setRs(29); // $sp
        instruction.setRt(8);  // $t0
        instruction.setOffsetIJ((short) 100);

        assertEquals("Type should be I", 'I', instruction.getType());
        assertEquals("Op should be 35", 35, instruction.getOp());
        assertEquals("Rs should be 29", 29, instruction.getRs());
        assertEquals("Rt should be 8", 8, instruction.getRt());
        assertEquals("Offset should be 100", 100, instruction.getOffsetIJ());
    }

    @Test
    public void testJTypeInstruction() {
        // Set up J-type instruction
        instruction.setType('J');
        instruction.setOp(2); // J
        instruction.setOffsetIJ((short) 1000);

        assertEquals("Type should be J", 'J', instruction.getType());
        assertEquals("Op should be 2", 2, instruction.getOp());
        assertEquals("Offset should be 1000", 1000, instruction.getOffsetIJ());
    }

    @Test
    public void testNOPInstruction() {
        instruction.setType('N');
        instruction.setString("NOP");

        assertEquals("Type should be N", 'N', instruction.getType());
        assertEquals("String should be NOP", "NOP", instruction.getiString());
    }

    @Test
    public void testGetBitStringRType() {
        // Test R-type bit string generation
        instruction.setType('R');
        instruction.setOp(0);
        instruction.setRs(1);
        instruction.setRt(2);
        instruction.setRd(3);
        instruction.setShamt(0);
        instruction.setFunction(32);

        String bitString = instruction.getBitString();
        assertNotNull("Bit string should not be null", bitString);
        assertFalse("Bit string should not be empty", bitString.isEmpty());
    }

    @Test
    public void testGetBitStringIType() {
        // Test I-type bit string generation
        instruction.setType('I');
        instruction.setOp(35);
        instruction.setRs(29);
        instruction.setRt(8);
        instruction.setOffsetIJ((short) 100);

        String bitString = instruction.getBitString();
        assertNotNull("Bit string should not be null", bitString);
        assertFalse("Bit string should not be empty", bitString.isEmpty());
    }

    @Test
    public void testGetBitStringInvalidType() {
        // Test invalid instruction type
        instruction.setType('U');

        String bitString = instruction.getBitString();
        assertEquals("Invalid instruction should return error message", "INVALID INSTRUCTION", bitString);
    }

    @Test
    public void testSerializableInterface() {
        assertTrue("Should implement Serializable", instruction instanceof java.io.Serializable);
    }
}
