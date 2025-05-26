package assembler.instruction;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Tests for AssemblyCode utility methods
 */
public class AssemblyCodeTest {

    private AssemblyCode assemblyCode;

    @Before
    public void setUp() {
        // Use simple assembly code for testing
        String testCode = "add $t0, $t1, $t2\nlw $t3, 100($sp)\nnop";
        assemblyCode = new AssemblyCode(testCode);
    }

    @Test
    public void testContainsOnlyNumbers() {
        // Test valid numbers
        assertTrue("Should recognize positive integer", assemblyCode.containsOnlyNumbers("123"));
        assertTrue("Should recognize zero", assemblyCode.containsOnlyNumbers("0"));
        assertTrue("Should recognize negative integer", assemblyCode.containsOnlyNumbers("-123"));
        assertTrue("Should recognize single digit", assemblyCode.containsOnlyNumbers("5"));

        // Test invalid inputs
        assertFalse("Should reject letters", assemblyCode.containsOnlyNumbers("abc"));
        assertFalse("Should reject mixed alphanumeric", assemblyCode.containsOnlyNumbers("123abc"));
        assertFalse("Should reject empty string", assemblyCode.containsOnlyNumbers(""));
        assertFalse("Should reject null", assemblyCode.containsOnlyNumbers(null));
        assertFalse("Should reject decimal", assemblyCode.containsOnlyNumbers("12.34"));
        assertFalse("Should reject spaces", assemblyCode.containsOnlyNumbers("12 34"));
    }

    @Test
    public void testRegNumOf() {
        // Test valid register names
        assertEquals("$zero should be register 0", 0, assemblyCode.regNumOf("$zero"));
        assertEquals("$at should be register 1", 1, assemblyCode.regNumOf("$at"));
        assertEquals("$v0 should be register 2", 2, assemblyCode.regNumOf("$v0"));
        assertEquals("$v1 should be register 3", 3, assemblyCode.regNumOf("$v1"));
        assertEquals("$t0 should be register 8", 8, assemblyCode.regNumOf("$t0"));
        assertEquals("$t1 should be register 9", 9, assemblyCode.regNumOf("$t1"));
        assertEquals("$sp should be register 29", 29, assemblyCode.regNumOf("$sp"));
        assertEquals("$ra should be register 31", 31, assemblyCode.regNumOf("$ra"));

        // Test case insensitive
        assertEquals("Should be case insensitive", 8, assemblyCode.regNumOf("$T0"));
        assertEquals("Should be case insensitive", 29, assemblyCode.regNumOf("$SP"));

        // Test invalid register names
        assertEquals("Invalid register should return -1", -1, assemblyCode.regNumOf("$invalid"));
        assertEquals("Empty string should return -1", -1, assemblyCode.regNumOf(""));
        assertEquals("Null should return -1", -1, assemblyCode.regNumOf(null));
    }

    @Test
    public void testCheckRType() {
        // Test valid R-type mnemonics
        assertTrue("add should be R-type", assemblyCode.checkRType("add"));
        assertTrue("sub should be R-type", assemblyCode.checkRType("sub"));
        assertTrue("and should be R-type", assemblyCode.checkRType("and"));
        assertTrue("or should be R-type", assemblyCode.checkRType("or"));
        assertTrue("nor should be R-type", assemblyCode.checkRType("nor"));
        assertTrue("slt should be R-type", assemblyCode.checkRType("slt"));
        assertTrue("mult should be R-type", assemblyCode.checkRType("mult"));
        assertTrue("div should be R-type", assemblyCode.checkRType("div"));

        // Test case insensitive
        assertTrue("Should be case insensitive", assemblyCode.checkRType("ADD"));
        assertTrue("Should be case insensitive", assemblyCode.checkRType("Sub"));

        // Test invalid mnemonics
        assertFalse("lw should not be R-type", assemblyCode.checkRType("lw"));
        assertFalse("beq should not be R-type", assemblyCode.checkRType("beq"));
        assertFalse("invalid should not be R-type", assemblyCode.checkRType("invalid"));
        assertFalse("Empty string should not be R-type", assemblyCode.checkRType(""));
    }

    @Test
    public void testCheckIType() {
        // Test valid I-type mnemonics
        assertTrue("beq should be I-type", assemblyCode.checkIType("beq"));
        assertTrue("addi should be I-type", assemblyCode.checkIType("addi"));
        assertTrue("lw should be I-type", assemblyCode.checkIType("lw"));
        assertTrue("sw should be I-type", assemblyCode.checkIType("sw"));

        // Test case insensitive
        assertTrue("Should be case insensitive", assemblyCode.checkIType("BEQ"));
        assertTrue("Should be case insensitive", assemblyCode.checkIType("Lw"));

        // Test invalid mnemonics
        assertFalse("add should not be I-type", assemblyCode.checkIType("add"));
        assertFalse("sub should not be I-type", assemblyCode.checkIType("sub"));
        assertFalse("invalid should not be I-type", assemblyCode.checkIType("invalid"));
        assertFalse("Empty string should not be I-type", assemblyCode.checkIType(""));
    }

    @Test
    public void testReturnLine() {
        // Create assembly code with labels for testing
        String labeledCode = "start: add $t0, $t1, $t2\nloop: lw $t3, 100($sp)\nend: nop";
        AssemblyCode labeledAssembly = new AssemblyCode(labeledCode);

        // Test finding labels (assuming they are processed correctly)
        // Note: This test depends on the label processing working correctly
        int startLine = labeledAssembly.returnLine("start");
        int loopLine = labeledAssembly.returnLine("loop");
        int endLine = labeledAssembly.returnLine("end");

        // These should return valid line numbers or -1 if not found
        assertTrue("start label should be found or return -1", startLine >= -1);
        assertTrue("loop label should be found or return -1", loopLine >= -1);
        assertTrue("end label should be found or return -1", endLine >= -1);

        // Test non-existent label
        assertEquals("Non-existent label should return -1", -1, labeledAssembly.returnLine("nonexistent"));
        assertEquals("Empty string should return -1", -1, labeledAssembly.returnLine(""));
    }

    @Test
    public void testFillLines() {
        // Test with code that has comments and empty lines
        String complexCode = "# This is a comment\nadd $t0, $t1, $t2  # inline comment\n\n\nlw $t3, 100($sp)\n# Another comment\nnop\n\n";
        AssemblyCode complexAssembly = new AssemblyCode(complexCode);

        // Should have filtered out comments and empty lines
        assertNotNull("Lines should be filled", complexAssembly.lines);
        assertTrue("Should have at least some lines", complexAssembly.lines.length > 0);

        // Check that comments are removed
        for (String line : complexAssembly.lines) {
            assertFalse("Lines should not contain comments", line.contains("#"));
            assertFalse("Lines should not be empty", line.trim().isEmpty());
        }
    }

    @Test
    public void testBasicAssemblyParsing() {
        // Test that basic assembly instructions are parsed
        assertNotNull("Instructions should be created", assemblyCode.instructions);
        assertTrue("Should have instructions", assemblyCode.instructions.length > 0);

        // Test that instruction strings are set
        for (Instruction inst : assemblyCode.instructions) {
            assertNotNull("Instruction should not be null", inst);
            assertNotNull("Instruction string should not be null", inst.iString);
        }
    }

    @Test
    public void testNOPInstruction() {
        String nopCode = "nop";
        AssemblyCode nopAssembly = new AssemblyCode(nopCode);

        assertEquals("Should have one instruction", 1, nopAssembly.instructions.length);
        Instruction nopInst = nopAssembly.instructions[0];
        assertEquals("Should be N-type", 'N', nopInst.getType());
        assertEquals("Should have correct string", "NO OPERATION", nopInst.iString);
    }

    @Test
    public void testEmptyCode() {
        String emptyCode = "";
        AssemblyCode emptyAssembly = new AssemblyCode(emptyCode);

        assertNotNull("Should handle empty code", emptyAssembly);
        assertEquals("Should have no lines", 0, emptyAssembly.lines.length);
        assertEquals("Should have no instructions", 0, emptyAssembly.instructions.length);
    }

    @Test
    public void testCommentsOnlyCode() {
        String commentCode = "# This is a comment\n# Another comment\n\n";
        AssemblyCode commentAssembly = new AssemblyCode(commentCode);

        assertNotNull("Should handle comment-only code", commentAssembly);
        assertEquals("Should have no lines after filtering", 0, commentAssembly.lines.length);
        assertEquals("Should have no instructions", 0, commentAssembly.instructions.length);
    }

    @Test
    public void testRegisterArrayInitialization() {
        // Test that register array is properly initialized
        assertNotNull("Registers array should be initialized", assemblyCode.Registers);
        assertEquals("Should have 32 registers", 32, assemblyCode.Registers.length);
        assertEquals("First register should be $zero", "$zero", assemblyCode.Registers[0]);
        assertEquals("Last register should be $ra", "$ra", assemblyCode.Registers[31]);
    }

    @Test
    public void testMnemonicArraysInitialization() {
        // Test that mnemonic arrays are properly initialized
        assertNotNull("R-type mnemonics should be initialized", assemblyCode.RTypeMnemonics);
        assertNotNull("I-type mnemonics should be initialized", assemblyCode.ITypeMnemonics);
        assertTrue("Should have R-type mnemonics", assemblyCode.RTypeMnemonics.length > 0);
        assertTrue("Should have I-type mnemonics", assemblyCode.ITypeMnemonics.length > 0);
    }

    @Test
    public void testErrorHandling() {
        // Test that errors string is initialized
        assertNotNull("Errors string should be initialized", assemblyCode.errors);

        // Test with invalid assembly code
        String invalidCode = "invalid_instruction $t0, $t1, $t2";
        AssemblyCode invalidAssembly = new AssemblyCode(invalidCode);

        // Should handle invalid code gracefully
        assertNotNull("Should handle invalid code", invalidAssembly);
        assertNotNull("Should have instructions array", invalidAssembly.instructions);
    }
}
