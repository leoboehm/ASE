package MathPlot.Calculators.Operators.BinaryOperations;

import MathPlot.Calculators.Node;
import MathPlot.Calculators.Operators.BinaryOperation;
import MathPlot.Calculators.Operators.Constant;

public class Div extends BinaryOperation {
    public Div(Node l, Node r) {
        super(l, r);
    }

    public double eval(double v) {
        return left.eval(v) / right.eval(v);
    }

    public Node calculateDerivative() {
        return new Div(new Sub(new Mult(left.calculateDerivative(), right), new Mult(left, right.calculateDerivative())), new Power(right, new Constant(2)));
    }

    public Node simplify() {
        return new Div(left.simplify(), right.simplify());
    }
}
