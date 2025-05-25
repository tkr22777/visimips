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
} 