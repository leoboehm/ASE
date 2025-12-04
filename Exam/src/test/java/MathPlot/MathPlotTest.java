package MathPlot;

import MathPlot.Calculators.Operators.*;
import org.junit.jupiter.api.Test;
import javafx.scene.canvas.Canvas;

import static org.junit.jupiter.api.Assertions.*;

public class MathPlotTest {
    @Test
    void testSetExpression() {
        MathPlot mp = new MathPlot();
        mp.setExpression("(x + 3)", MathPlot.ExpressionFormat.AOS);
        assertNotNull(mp.print(MathPlot.ExpressionFormat.AOS));
    }
    @Test
    void testPrint() {
        MathPlot mp = new MathPlot();
        mp.setExpression("5", MathPlot.ExpressionFormat.AOS);
        var list = mp.print(MathPlot.ExpressionFormat.AOS);
        assertEquals(2, list.size());
        assertTrue(list.get(0).contains("Function"));
        assertTrue(list.get(1).contains("Derivative"));
    }

    // test area calculation
    @Test
    void testAreaRectangular() {
        MathPlot mp = new MathPlot();
        mp.setExpression("x", MathPlot.ExpressionFormat.AOS);
        double area = mp.area(MathPlot.AreaType.Rectangular);
        assertTrue(area > 0);
    }
    @Test
    void testAreaTrapezoidal() {
        MathPlot mp = new MathPlot();
        mp.setExpression("x", MathPlot.ExpressionFormat.AOS);
        double area = mp.area(MathPlot.AreaType.Trapezoidal);
        assertTrue(area > 0);
    }

    // test plotting
    @Test
    void testCartesianPlot() {
        MathPlot mp = new MathPlot();
        mp.setExpression("x", MathPlot.ExpressionFormat.AOS);

        Canvas c = new Canvas(300, 300);
        // just test for no exception
        assertDoesNotThrow(() -> mp.plot(c, MathPlot.PlotType.Cartesian));
    }
    @Test
    void testPolarPlot() {
        MathPlot mp = new MathPlot();
        mp.setExpression("x", MathPlot.ExpressionFormat.AOS);

        Canvas c = new Canvas(300, 300);
        // just test for no exception
        assertDoesNotThrow(() -> mp.plot(c, MathPlot.PlotType.Polar));
    }
}