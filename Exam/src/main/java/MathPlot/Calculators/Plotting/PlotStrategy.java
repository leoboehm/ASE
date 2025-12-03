package MathPlot.Calculators.Plotting;

import MathPlot.Calculators.Node;
import MathPlot.PlotterInterface;

public interface PlotStrategy {
    void plot(PlotterInterface plotter, Node function, Node derivative);
}
