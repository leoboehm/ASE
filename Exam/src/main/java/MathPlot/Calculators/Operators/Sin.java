package MathPlot.Calculators.Operators;

import MathPlot.Calculators.Node;
import MathPlot.Calculators.Operators.BinaryOperations.Mult;

public class Sin implements Node {
    public Node child;

    public Sin(Node c) {
        this.child = c;
    }

    public double eval(double v) {
        return Math.sin(child.eval(v));
    }

    public Node calculateDerivative() {
        return new Mult(new Cos(child), child.calculateDerivative());
    } // Kettenregel

    public Node simplify() {
        return new Sin(child.simplify());
    }
}
