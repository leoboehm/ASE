package MathPlot.Calculators.Operators;

import MathPlot.Calculators.Node;

public class Variable implements Node {
    public double eval(double v) {
        return v;
    }

    public Node calculateDerivative() {
        return new Constant(1);
    }

    public Node simplify() {
        return this;
    }
}
