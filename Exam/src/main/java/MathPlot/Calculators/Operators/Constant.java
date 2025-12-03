package MathPlot.Calculators.Operators;

import MathPlot.Calculators.Node;

public class Constant implements Node {
    private final double value;

    public Constant(double value) {
        this.value = value;
    }

    public double eval(double v) {
        return value;
    }

    public Node calculateDerivative() {
        return new Constant(0);
    }

    public Node simplify() {
        return this;
    }
}
