package MathPlot;

import MathPlot.Calculators.Node;
import MathPlot.Calculators.Operators.*;
import MathPlot.Calculators.Operators.BinaryOperations.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {
    // constant & variable
    @Test
    void testConstantEval() {
        assertEquals(7.5, new Constant(7.5).eval(123));
    }
    @Test
    void testVariableEval() {
        assertEquals(3.14, new Variable().eval(3.14));
    }
    @Test
    void testVariableSimplify() {
        assertTrue(new Variable().simplify() instanceof Variable);
    }

    // add
    @Test
    void testAddEval() {
        Node n = new Add(new Constant(3), new Constant(4));
        assertEquals(7.0, n.eval(0));
    }
    @Test
    void testAddDerivative() {
        Node n = new Add(new Variable(), new Constant(5));
        assertEquals(1.0, n.calculateDerivative().eval(10));
    }
    @Test
    void testAddSimplifyLeftZero() {
        Node n = new Add(new Constant(0), new Constant(9));
        assertEquals(9.0, n.simplify().eval(0));
    }
    @Test
    void testAddSimplifyRightZero() {
        Node n = new Add(new Constant(5), new Constant(0));
        assertEquals(5.0, n.simplify().eval(0));
    }

    // subtract
    @Test
    void testSubEvalSimple() {
        Node n = new Sub(new Constant(10), new Constant(4));
        assertEquals(6.0, n.eval(0));
    }
    @Test
    void testSubDerivative() {
        Node n = new Sub(new Variable(), new Constant(3));
        assertEquals(1.0, n.calculateDerivative().eval(0));
    }
    @Test
    void testSubSimplifyZero() {
        Node n = new Sub(new Variable(), new Constant(0));
        assertTrue(n.simplify() instanceof Variable);
    }

    // multiply
    @Test
    void testMultEval() {
        Node n = new Mult(new Constant(3), new Constant(4));
        assertEquals(12.0, n.eval(0));
    }
    @Test
    void testMultDerivative() {
        // derivative of x * x = 2x
        Node m = new Mult(new Variable(), new Variable());
        assertEquals(4.0, m.calculateDerivative().eval(2));
    }
    @Test
    void testMultSimplifyZeroLeft() {
        Node n = new Mult(new Constant(0), new Variable());
        assertEquals(0.0, n.simplify().eval(5));
    }
    @Test
    void testMultSimplifyZeroRight() {
        Node n = new Mult(new Variable(), new Constant(0));
        assertEquals(0.0, n.simplify().eval(5));
    }
    @Test
    void testMultSimplifyOneLeft() {
        Node n = new Mult(new Constant(1), new Variable());
        assertTrue(n.simplify() instanceof Variable);
    }
    @Test
    void testMultSimplifyOneRight() {
        Node n = new Mult(new Variable(), new Constant(1));
        assertTrue(n.simplify() instanceof Variable);
    }

    // divide
    @Test
    void testDivEval() {
        Node n = new Div(new Constant(12), new Constant(3));
        assertEquals(4.0, n.eval(0));
    }
    @Test
    void testDivDerivativeQuotientRule() {
        // derivative of x / 2 = 1/2
        Node d = new Div(new Variable(), new Constant(2));
        assertEquals(0.5, d.calculateDerivative().eval(10));
    }
    @Test
    void testDivByZeroReturnsInfinity() {
        Node n = new Div(new Constant(5), new Constant(0));
        assertTrue(Double.isInfinite(n.eval(0)));
    }

    // power
    @Test
    void testPowerEval() {
        Node n = new Power(new Constant(2), new Constant(3));
        assertEquals(8.0, n.eval(0));
    }
    @Test
    void testPowerDerivativeConstantExponent() {
        // derivative of x^4 = 4 * x^3
        Node p = new Power(new Variable(), new Constant(4));
        assertEquals(4 * 3 * 3 * 3, p.calculateDerivative().eval(3));
    }
    @Test
    void testPowerSimplifyExponentZero() {
        Node p = new Power(new Variable(), new Constant(0));
        assertEquals(1.0, p.simplify().eval(1000));
    }
    @Test
    void testPowerSimplifyExponentOne() {
        Node p = new Power(new Variable(), new Constant(1));
        assertTrue(p.simplify() instanceof Variable);
    }

    // sin
    @Test
    void testSinEval() {
        Node s = new Sin(new Constant(Math.PI / 2));
        assertEquals(1.0, s.eval(0), 1e-9);
    }
    @Test
    void testSinDerivative() {
        Node s = new Sin(new Variable());
        assertEquals(Math.cos(1), s.calculateDerivative().eval(1), 1e-9);
    }
    @Test
    void testSinSimplify() {
        Node s = new Sin(new Constant(0));
        assertEquals(0.0, s.simplify().eval(0));
    }

    // cos
    @Test
    void testCosEval() {
        Node c = new Cos(new Constant(0));
        assertEquals(1.0, c.eval(0));
    }
    @Test
    void testCosDerivative() {
        Node c = new Cos(new Variable());
        assertEquals(-Math.sin(2), c.calculateDerivative().eval(2), 1e-9);
    }
    @Test
    void testCosSimplify() {
        Node c = new Cos(new Constant(Math.PI));
        assertEquals(-1.0, c.simplify().eval(0), 1e-9);
    }

    // nested expression (f(g(x)))
    @Test
    void testNestedDerivative() {
        // derivative of sin(x^2) = cos(x^2) * 2x
        Node f = new Sin(new Power(new Variable(), new Constant(2)));
        Node d = f.calculateDerivative();
        assertEquals(Math.cos(9) * 6, d.eval(3), 1e-9);
    }
}