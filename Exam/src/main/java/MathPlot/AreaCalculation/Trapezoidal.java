package MathPlot.AreaCalculation;

import MathPlot.Calculators.Node;

public class Trapezoidal implements AreaStrategy {
    public double calculate(Node func, double start, double end, double step) {
        double area = 0;
        for (double x = start; x < end; x += step) {
            double y1 = func.eval(x);
            double y2 = func.eval(x + step);
            area += (y1 + y2) / 2 * step;
        }
        return area;
    }
}