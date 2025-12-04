package MathPlot.Plotting;

import MathPlot.Calculators.Node;
import MathPlot.PlotterInterface;
import MathPlot.Point;
import javafx.scene.paint.Color;

public class Cartesian implements PlotStrategy {
    public void plot(PlotterInterface plotter, Node func, Node deriv) {
        drawCurve(plotter, func, Color.BLUE);
        drawCurve(plotter, deriv, Color.RED);
        // Grid und Achsen
        plotter.addLine(new Point(-100, 0), new Point(100, 0), Color.LIGHTGRAY, 0.1); // X-Achse
        plotter.addLine(new Point(0, -100), new Point(0, 100), Color.LIGHTGRAY, 0.1); // Y-Achse
    }

    private void drawCurve(PlotterInterface plotter, Node node, Color c) {
        plotter.addCurve(new FunctionIterator(node, -10, 10, 0.1), c, 0.2);
    }

    // Iterator Implementation
    class FunctionIterator implements Point.Iterator {
        private Node node;
        private double start, end, step, current;

        public FunctionIterator(Node n, double s, double e, double st) {
            this.node = n;
            start = s;
            end = e;
            step = st;
            current = s;
        }

        public boolean hasNext() {
            return current <= end;
        }

        public Point nextPoint() {
            Point p = new Point(current, node.eval(current));
            current += step;
            return p;
        }

        public void reset() {
            current = start;
        }

        public boolean hasBreak() {
            return false;
        }
    }
}
