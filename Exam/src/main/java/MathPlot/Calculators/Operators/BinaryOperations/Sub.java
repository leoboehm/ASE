package MathPlot.Calculators.Operators.BinaryOperations;

import MathPlot.Calculators.Node;
import MathPlot.Calculators.Operators.BinaryOperation;
import MathPlot.Calculators.Operators.Constant;

public class Sub extends BinaryOperation {
    public Sub(Node l, Node r) {
        super(l, r);
    }

    public double eval(double v) {
        return left.eval(v) - right.eval(v);
    }

    public Node calculateDerivative() {
        return new Sub(left.calculateDerivative(), right.calculateDerivative());
    }

    public Node simplify() {
        Node r = right.simplify();
        if (r instanceof Constant && r.eval(0) == 0) return left.simplify();
        return new Sub(left.simplify(), r);
    }
}
