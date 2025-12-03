package MathPlot.AreaCalculation;

import MathPlot.Calculators.Node;

public class Rectangular implements AreaStrategy {
    public double calculate(Node func, double start, double end, double step) {
        double area = 0;
        for (double x = start; x < end; x += step) {
            area += func.eval(x) * step;
        }
        return area;
    }
}