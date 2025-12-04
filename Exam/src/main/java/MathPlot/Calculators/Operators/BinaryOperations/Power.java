package MathPlot.Calculators.Operators.BinaryOperations;

import MathPlot.Calculators.Node;
import MathPlot.Calculators.Operators.BinaryOperation;
import MathPlot.Calculators.Operators.Constant;

public class Power extends BinaryOperation {
    public Power(Node l, Node r) {
        super(l, r);
    }

    public double eval(double v) {
        return Math.pow(left.eval(v), right.eval(v));
    }

    public Node calculateDerivative() {
        double n = right.eval(0);
        return new Mult(new Mult(right, new Power(left, new Constant(n - 1))), left.calculateDerivative());
    }

    public Node simplify() {
        Node r = right.simplify();
        if (r instanceof Constant && r.eval(0) == 1) return left.simplify();
        if (r instanceof Constant && r.eval(0) == 0) return new Constant(1);
        return new Power(left.simplify(), r);
    }
}
