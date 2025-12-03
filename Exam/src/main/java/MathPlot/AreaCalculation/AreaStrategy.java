package MathPlot.AreaCalculation;

import MathPlot.Calculators.Node;

public interface AreaStrategy {
    double calculate(Node function, double start, double end, double step);
}
