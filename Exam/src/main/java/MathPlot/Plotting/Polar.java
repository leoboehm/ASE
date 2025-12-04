package MathPlot.Plotting;

import MathPlot.Calculators.Node;
import MathPlot.PlotterInterface;
import MathPlot.Point;
import javafx.scene.paint.Color;

public class Polar implements PlotStrategy {
    public void plot(PlotterInterface plotter, Node func, Node deriv) {
        drawPolar(plotter, func, Color.BLUE);
        drawPolar(plotter, deriv, Color.RED);
        // Grid
        int circleCount = 4;
        double rMax = 100;
        for (int i = 1; i <= circleCount; ++i) {
            double r = (rMax * i) / circleCount;
            plotter.addCircle(new Point(0.0, 0.0), r, Color.LIGHTGRAY, 0.05);
        }
        int radialLines = 8;
        for (int i = 0; i < radialLines; ++i) {
            double theta = (2 * Math.PI * i) / radialLines;
            double x = rMax * Math.cos(theta);
            double y = rMax * Math.sin(theta);
            plotter.addLine(new Point(0.0, 0.0), new Point(x, y), Color.LIGHTGRAY, 0.05);
        }
    }

    private void drawPolar(PlotterInterface plotter, Node node, Color c) {
        plotter.addCurve(new PolarIterator(node, 0, Math.PI * 4, 0.1), c, 0.2);
    }

    class PolarIterator implements Point.Iterator {
        private Node node;
        private double start, end, step, current;

        public PolarIterator(Node n, double s, double e, double st) {
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
            double r = node.eval(current);
            double x = r * Math.cos(current);
            double y = r * Math.sin(current);
            current += step;
            return new Point(x, y);
        }

        public void reset() {
            current = start;
        }

        public boolean hasBreak() {
            return false;
        }
    }
}
