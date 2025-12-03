package MathPlot.Calculators.Operators.BinaryOperations;

import MathPlot.Calculators.Node;
import MathPlot.Calculators.Operators.BinaryOperation;
import MathPlot.Calculators.Operators.Constant;

public class Add extends BinaryOperation {
    public Add(Node l, Node r) {
        super(l, r);
    }

    public double eval(double v) {
        return left.eval(v) + right.eval(v);
    }

    public Node calculateDerivative() {
        return new Add(left.calculateDerivative(), right.calculateDerivative());
    }

    public Node simplify() {
        Node l = left.simplify();
        Node r = right.simplify();
        if (l instanceof Constant && l.eval(0) == 0) return r;
        if (r instanceof Constant && r.eval(0) == 0) return l;
        return new Add(l, r);
    }
}
