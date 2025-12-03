package MathPlot.Calculators.Operators;

import MathPlot.Calculators.Node;

public abstract class BinaryOperation implements Node {
    public Node left;
    public Node right;

    public BinaryOperation(Node left, Node right) {
        this.left = left;
        this.right = right;
    }
}
