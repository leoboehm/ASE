package MathPlot;

import MathPlot.Parsers.AOS;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMathPlot {

    @Test
    public void testCalculateDerivative_simpleExpression() throws Exception {
        MathPlot mathPlot = new MathPlot();

        AOS parser = new AOS();
        AOS.Parts parsed = parser.parse("x^2 + sin(x)");

        String expected = "((2 * x^1) + (cos(x) * 1))";
        String actual = mathPlot.calculateDerivative(parsed);

        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateDerivative_constant() throws Exception {
        MathPlot mathPlot = new MathPlot();

        AOS parser = new AOS();
        AOS.Parts parsed = parser.parse("5");

        String expected = "0";
        String actual = mathPlot.calculateDerivative(parsed);

        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateDerivative_variable() throws Exception {
        MathPlot mathPlot = new MathPlot();

        AOS parser = new AOS();
        AOS.Parts parsed = parser.parse("x");

        String expected = "1";
        String actual = mathPlot.calculateDerivative(parsed);

        assertEquals(expected, actual);
    }
}
