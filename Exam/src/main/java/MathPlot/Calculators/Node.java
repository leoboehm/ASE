package MathPlot.Calculators;

public interface Node {
    double eval(double value);

    Node calculateDerivative();

    Node simplify();
}
