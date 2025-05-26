package assembler;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ALUTest {

    private ALU alu;

    @Before
    public void setUp() {
        alu = new ALU();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull("ALU object should be created", alu);
    }

    @Test
    public void testAddOperation() {
        int result = alu.process(10, 5, 0, 0); // ALUOp = 0 (ADD)
        assertEquals("ADD operation failed", 15, result);
    }

    @Test
    public void testSubOperation() {
        int result = alu.process(10, 5, 0, 1); // ALUOp = 1 (SUB)
        assertEquals("SUB operation failed", 5, result);
    }

    @Test
    public void testAndOperation() {
        int result = alu.process(15, 7, 36, 2); // Function = 36 (AND), ALUOp = 2
        assertEquals("AND operation failed", 7, result);
    }

    @Test
    public void testOrOperation() {
        int result = alu.process(5, 3, 37, 2); // Function = 37 (OR), ALUOp = 2
        assertEquals("OR operation failed", 7, result);
    }

    @Test
    public void testSltOperation() {
        int result = alu.process(5, 10, 42, 2); // Function = 42 (SLT), ALUOp = 2
        assertEquals("SLT operation failed (5 < 10)", 1, result);

        result = alu.process(10, 5, 42, 2);
        assertEquals("SLT operation failed (10 not < 5)", 0, result);
    }

    @Test
    public void testMultiplyOperation() {
        int result = alu.process(6, 7, 24, 2); // Function = 24 (MULT), ALUOp = 2
        assertEquals("MULT operation failed", 42, result);

        result = alu.process(-3, 4, 24, 2);
        assertEquals("MULT with negative failed", -12, result);

        result = alu.process(0, 100, 24, 2);
        assertEquals("MULT with zero failed", 0, result);
    }

    @Test
    public void testDivideOperation() {
        int result = alu.process(20, 4, 26, 2); // Function = 26 (DIV), ALUOp = 2
        assertEquals("DIV operation failed", 5, result);

        result = alu.process(-20, 4, 26, 2);
        assertEquals("DIV with negative failed", -5, result);

        result = alu.process(7, 3, 26, 2);
        assertEquals("DIV with remainder failed", 2, result);
    }

    @Test
    public void testRTypeAddOperation() {
        int result = alu.process(15, 25, 32, 2); // Function = 32 (R-type ADD), ALUOp = 2
        assertEquals("R-type ADD operation failed", 40, result);

        result = alu.process(-10, 5, 32, 2);
        assertEquals("R-type ADD with negative failed", -5, result);
    }

    @Test
    public void testRTypeSubOperation() {
        int result = alu.process(20, 8, 34, 2); // Function = 34 (R-type SUB), ALUOp = 2
        assertEquals("R-type SUB operation failed", 12, result);

        result = alu.process(5, 10, 34, 2);
        assertEquals("R-type SUB negative result failed", -5, result);
    }

    @Test
    public void testNorOperation() {
        int result = alu.process(5, 3, 39, 2); // Function = 39 (NOR), ALUOp = 2
        int expected = ~(5 | 3); // NOR is ~(A | B)
        assertEquals("NOR operation failed", expected, result);

        result = alu.process(0, 0, 39, 2);
        assertEquals("NOR with zeros failed", -1, result); // ~0 = -1
    }

    @Test
    public void testInvalidFunction() {
        int result = alu.process(10, 5, 99, 2); // Invalid function code
        assertEquals("Invalid function should return 0", 0, result);
    }

    @Test
    public void testInvalidALUOp() {
        int result = alu.process(10, 5, 0, 99); // Invalid ALUOp
        assertEquals("Invalid ALUOp should return 0", 0, result);
    }

    @Test
    public void testEdgeCases() {
        // Test with maximum values
        int result = alu.process(Integer.MAX_VALUE, 1, 32, 2);
        assertEquals("MAX_VALUE + 1 should overflow", Integer.MIN_VALUE, result);

        // Test with minimum values
        result = alu.process(Integer.MIN_VALUE, 1, 34, 2);
        assertEquals("MIN_VALUE - 1 should overflow", Integer.MAX_VALUE, result);

        // Test SLT with equal values
        result = alu.process(5, 5, 42, 2);
        assertEquals("SLT with equal values should return 0", 0, result);
    }

    @Test
    public void testDivisionByZero() {
        // Note: Original code doesn't handle division by zero
        // This test documents the current behavior
        try {
            int result = alu.process(10, 0, 26, 2);
            fail("Division by zero should throw ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected behavior
            assertTrue("Should throw ArithmeticException for division by zero", true);
        }
    }
}
