package MathPlot;

import MathPlot.MathPlot.AreaType;
import MathPlot.MathPlot.ExpressionFormat;
import MathPlot.Parsers.AOS;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestMathPlot {
    // simplification tests
    @Test
    void testExpressionSimplification() {
        MathPlot mp = new MathPlot();

        mp.setExpression("x + 0", ExpressionFormat.AOS);
        List<String> res = mp.print(ExpressionFormat.AOS);

        // x + 0 = x
        assertEquals("x", res.getFirst());
    }

    // derivative calculation tests
    @Test
    public void testCalculateDerivativeSimpleExpression() throws Exception {
        MathPlot mp = new MathPlot();

        AOS parser = new AOS();
        AOS.Parts parsed = parser.parse("x^2 + sin(x)");

        assertEquals("((2 * x^1) + (cos(x) * 1))", mp.calculateDerivativeAOS(parsed));
    }
    @Test
    public void testCalculateDerivativeConstant() throws Exception {
        MathPlot mp = new MathPlot();

        AOS parser = new AOS();
        AOS.Parts parsed = parser.parse("5");

        assertEquals("0", mp.calculateDerivativeAOS(parsed));
    }
    @Test
    public void testCalculateDerivativeVariable() throws Exception {
        MathPlot mp = new MathPlot();

        AOS parser = new AOS();
        AOS.Parts parsed = parser.parse("x");

        assertEquals("1", mp.calculateDerivativeAOS(parsed));
    }

    // area calculation tests
    @Test
    void testAreaOfLinearFunctionRectangular() {
        MathPlot mp = new MathPlot();
        mp.setExpression("x", ExpressionFormat.AOS);

        // integral x dx from -10 to 10 is 0
        assertEquals(0.0, mp.area(AreaType.Rectangular), 1e-2);
    }

    @Test
    void testAreaOfConstantFunctionTrapezoidal() {
        MathPlot mp = new MathPlot();
        mp.setExpression("2", ExpressionFormat.AOS);

        // integral 2 dx from -10 to 10 = 40
        assertEquals(40.0, mp.area(AreaType.Trapezoidal), 1e-1);
    }
}
