package MathPlot.Calculators.Operators.BinaryOperations;

import MathPlot.Calculators.Node;
import MathPlot.Calculators.Operators.BinaryOperation;
import MathPlot.Calculators.Operators.Constant;

public class Mult extends BinaryOperation {
    public Mult(Node l, Node r) {
        super(l, r);
    }

    public double eval(double v) {
        return left.eval(v) * right.eval(v);
    }

    public Node calculateDerivative() {
        return new Add(new Mult(left.calculateDerivative(), right), new Mult(left, right.calculateDerivative()));
    }

    public Node simplify() {
        Node l = left.simplify();
        Node r = right.simplify();
        if (l instanceof Constant && l.eval(0) == 0) return new Constant(0);
        if (r instanceof Constant && r.eval(0) == 0) return new Constant(0);
        if (l instanceof Constant && l.eval(0) == 1) return r;
        if (r instanceof Constant && r.eval(0) == 1) return l;
        return new Mult(l, r);
    }
}
