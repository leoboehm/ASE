package MathPlot.Calculators.Operators;

import MathPlot.Calculators.Node;
import MathPlot.Calculators.Operators.BinaryOperations.Mult;

public class Cos implements Node {
    public Node child;

    public Cos(Node c) {
        this.child = c;
    }

    public double eval(double v) {
        return Math.cos(child.eval(v));
    }

    public Node calculateDerivative() {
        return new Mult(new Mult(new Constant(-1), new Sin(child)), child.calculateDerivative());
    }

    public Node simplify() {
        return new Cos(child.simplify());
    }
}
