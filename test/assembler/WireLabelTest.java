package assembler;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.awt.Color;
import java.awt.BasicStroke;

/**
 * Tests for WireLabel class
 */
public class WireLabelTest {

    private WireLabel wireLabel;
    private int[] testX;
    private int[] testY;

    @Before
    public void setUp() {
        testX = new int[]{10, 20, 30, 40};
        testY = new int[]{15, 25, 35, 45};
        wireLabel = new WireLabel(testX, testY);
    }

    @Test
    public void testConstructor() {
        assertNotNull("WireLabel should be created", wireLabel);
        assertSame("X coordinates should be set", testX, wireLabel.x);
        assertSame("Y coordinates should be set", testY, wireLabel.y);
        assertEquals("Default color should be blue", Color.BLUE, wireLabel.color);
    }

    @Test
    public void testCoordinateArrays() {
        // Test with different array sizes
        int[] x1 = {0, 10, 20};
        int[] y1 = {5, 15, 25};
        WireLabel wire1 = new WireLabel(x1, y1);

        assertSame("X array should be assigned", x1, wire1.x);
        assertSame("Y array should be assigned", y1, wire1.y);
    }

    @Test
    public void testEmptyArrays() {
        int[] emptyX = {};
        int[] emptyY = {};
        WireLabel emptyWire = new WireLabel(emptyX, emptyY);

        assertNotNull("WireLabel should handle empty arrays", emptyWire);
        assertSame("Empty X array should be assigned", emptyX, emptyWire.x);
        assertSame("Empty Y array should be assigned", emptyY, emptyWire.y);
    }

    @Test
    public void testSinglePointArrays() {
        int[] singleX = {100};
        int[] singleY = {200};
        WireLabel singleWire = new WireLabel(singleX, singleY);

        assertNotNull("WireLabel should handle single point", singleWire);
        assertEquals("Single X coordinate should be set", 100, singleWire.x[0]);
        assertEquals("Single Y coordinate should be set", 200, singleWire.y[0]);
    }

    @Test
    public void testUnequalArrayLengths() {
        int[] longX = {1, 2, 3, 4, 5};
        int[] shortY = {10, 20};
        WireLabel unequalWire = new WireLabel(longX, shortY);

        assertNotNull("WireLabel should handle unequal array lengths", unequalWire);
        assertEquals("X array length should be 5", 5, unequalWire.x.length);
        assertEquals("Y array length should be 2", 2, unequalWire.y.length);
    }

    @Test
    public void testColorProperty() {
        // Test default color
        assertEquals("Default color should be blue", Color.BLUE, wireLabel.color);

        // Test setting different colors
        Color[] testColors = {Color.RED, Color.GREEN, Color.BLACK, Color.WHITE, Color.YELLOW};
        for (Color color : testColors) {
            wireLabel.color = color;
            assertEquals("Color should be set to " + color, color, wireLabel.color);
        }
    }

    @Test
    public void testHardStroke() {
        // Get initial stroke
        BasicStroke initialStroke = wireLabel.stroke;

        // Apply hard stroke
        wireLabel.hardStroke();

        // Verify stroke changed
        assertNotSame("Stroke should change after hardStroke()", initialStroke, wireLabel.stroke);
        assertEquals("Hard stroke width should be 2.5f", 2.5f, wireLabel.stroke.getLineWidth(), 0.01f);
    }

    @Test
    public void testNormalStroke() {
        // First apply hard stroke
        wireLabel.hardStroke();
        BasicStroke hardStroke = wireLabel.stroke;

        // Then apply normal stroke
        wireLabel.normalStroke();

        // Verify stroke changed back
        assertNotSame("Stroke should change after normalStroke()", hardStroke, wireLabel.stroke);
        assertEquals("Normal stroke width should be 1.0f", 1.0f, wireLabel.stroke.getLineWidth(), 0.01f);
    }

    @Test
    public void testStrokeToggling() {
        // Test multiple toggles between hard and normal stroke
        float normalWidth = wireLabel.stroke.getLineWidth();

        wireLabel.hardStroke();
        float hardWidth = wireLabel.stroke.getLineWidth();
        assertTrue("Hard stroke should be wider than normal", hardWidth > normalWidth);

        wireLabel.normalStroke();
        assertEquals("Should return to normal width", normalWidth, wireLabel.stroke.getLineWidth(), 0.01f);

        wireLabel.hardStroke();
        assertEquals("Should return to hard width", hardWidth, wireLabel.stroke.getLineWidth(), 0.01f);
    }

    @Test
    public void testInheritanceFromJLabel() {
        assertTrue("WireLabel should extend JLabel", wireLabel instanceof javax.swing.JLabel);
    }

    @Test
    public void testCoordinateModification() {
        // Test that we can modify coordinates after creation
        int[] newX = {100, 200, 300};
        int[] newY = {150, 250, 350};

        wireLabel.x = newX;
        wireLabel.y = newY;

        assertSame("X coordinates should be updated", newX, wireLabel.x);
        assertSame("Y coordinates should be updated", newY, wireLabel.y);
    }

    @Test
    public void testNullArrayHandling() {
        // Test with null arrays (edge case)
        WireLabel nullWire = new WireLabel(null, null);

        assertNotNull("WireLabel should be created even with null arrays", nullWire);
        assertNull("X should be null", nullWire.x);
        assertNull("Y should be null", nullWire.y);
    }

    @Test
    public void testLargeCoordinateArrays() {
        // Test with larger arrays
        int[] largeX = new int[1000];
        int[] largeY = new int[1000];

        for (int i = 0; i < 1000; i++) {
            largeX[i] = i;
            largeY[i] = i * 2;
        }

        WireLabel largeWire = new WireLabel(largeX, largeY);
        assertNotNull("WireLabel should handle large arrays", largeWire);
        assertEquals("Large X array should be set", 1000, largeWire.x.length);
        assertEquals("Large Y array should be set", 1000, largeWire.y.length);
    }

    @Test
    public void testNegativeCoordinates() {
        int[] negativeX = {-10, -20, -30};
        int[] negativeY = {-15, -25, -35};
        WireLabel negativeWire = new WireLabel(negativeX, negativeY);

        assertNotNull("WireLabel should handle negative coordinates", negativeWire);
        assertEquals("Negative X coordinate should be set", -10, negativeWire.x[0]);
        assertEquals("Negative Y coordinate should be set", -15, negativeWire.y[0]);
    }
}
