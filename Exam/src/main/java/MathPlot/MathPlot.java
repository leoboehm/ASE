package MathPlot;

import java.util.ArrayList;
import java.util.List;

import MathPlot.Parsers.AOS;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class MathPlot {
    private class Plotter implements PlotterInterface {
        private interface PlotterItem {
            void plot();
        }

        private abstract class PlotterBase implements PlotterItem {
            final protected GraphicsContext gc;
            final protected Color color;
            final protected double lineWidth;

            public PlotterBase(Color color, double lineWidth) {
                this.gc = Plotter.this.canvas.getGraphicsContext2D();
                this.color = color;
                this.lineWidth = lineWidth;
            }
        }

        final List<PlotterItem> items;
        final private Canvas canvas;
        private Point min, max;
        private Point lastMouse;

        private class Circle extends PlotterBase {
            final private Point c;
            final private double r;

            public Circle(Point c, double r, Color color, double lineWidth) {
                super(color, lineWidth);

                this.c = c;
                this.r = r;
            }

            @Override
            public void plot() {
                this.gc.setStroke(this.color);
                this.gc.setLineWidth(this.lineWidth);
                this.gc.strokeOval(-this.r + this.c.x(), -this.r + this.c.y(), 2 * this.r + this.c.x(),
                        2 * this.r + this.c.y());
            }
        }

        private class Curve extends PlotterBase {
            final private Point.Iterator ptIt;

            public Curve(Point.Iterator ptIt, Color color, double lineWidth) {
                super(color, lineWidth);

                this.ptIt = ptIt;
            }

            @Override
            public void plot() {
                this.ptIt.reset();

                if (!this.ptIt.hasNext()) {
                    return;
                }

                this.gc.setLineWidth(this.lineWidth);
                this.gc.setStroke(this.color);

                this.gc.beginPath();

                Point origin = this.ptIt.nextPoint();
                this.gc.moveTo(origin.x(), origin.y());

                while (this.ptIt.hasNext()) {
                    final Point np = this.ptIt.nextPoint();

                    if (!this.ptIt.hasBreak()) {
                        this.gc.lineTo(np.x(), np.y());
                    }

                    this.gc.moveTo(np.x(), np.y());
                }

                this.gc.stroke();
            }
        }

        private class Line extends PlotterBase {
            final private Point from;
            final private Point to;

            public Line(Point from, Point to, Color color, double lineWidth) {
                super(color, lineWidth);

                this.from = from;
                this.to = to;
            }

            @Override
            public void plot() {
                this.gc.setStroke(this.color);
                this.gc.setLineWidth(this.lineWidth);
                this.gc.strokeLine(this.from.x(), this.from.y(), this.to.x(), this.to.y());
            }
        }

        public Plotter(Canvas canvas, Point min, Point max) {
            this.min = min;
            this.max = max;
            this.items = new ArrayList<>();
            this.canvas = canvas;

            this.canvas.widthProperty().addListener(_ -> render());
            this.canvas.heightProperty().addListener(_ -> render());

            this.canvas.setOnMousePressed(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    this.lastMouse = new Point(e.getX(), e.getY());
                }
            });

            this.canvas.setOnMouseDragged(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    double dx = e.getX() - this.lastMouse.x();
                    double dy = e.getY() - this.lastMouse.y();

                    double width = this.canvas.getWidth();
                    double height = this.canvas.getHeight();

                    double dxUnits = (dx / width) * (this.max.x() - this.min.x());
                    double dyUnits = (dy / height) * (this.max.y() - this.min.y());

                    this.min = new Point(this.min.x() - dxUnits, this.min.y() + dyUnits);
                    this.max = new Point(this.max.x() - dxUnits, this.max.y() + dyUnits);
                    this.lastMouse = new Point(e.getX(), e.getY());

                    render();
                }
            });

            this.canvas.addEventHandler(ScrollEvent.SCROLL, e -> {
                double zoomFactor = (e.getDeltaY() > 0) ? 0.9 : 1.1;

                double mouseX = e.getX();
                double mouseY = e.getY();

                double width = this.canvas.getWidth();
                double height = this.canvas.getHeight();

                double mouseXUnit = this.min.x() + (mouseX / width) * (this.max.x() - this.min.x());
                double mouseYUnit = this.max.y() - (mouseY / height) * (this.max.y() - this.min.y());

                double newWidth = (this.max.x() - this.min.x()) * zoomFactor;
                double newHeight = (this.max.y() - this.min.y()) * zoomFactor;

                double xMin = mouseXUnit - (mouseX - 0) / width * newWidth;
                double xMax = this.min.x() + newWidth;

                double yMax = mouseYUnit + (mouseY - 0) / height * newHeight;
                double yMin = this.max.y() - newHeight;

                this.min = new Point(xMin, yMin);
                this.max = new Point(xMax, yMax);

                render();
            });
        }

        @Override
        public void addCircle(Point c, double r, Color color, double lineWidth) {
            this.items.add(new Circle(c, r, color, lineWidth));
        }

        @Override
        public void addLine(Point from, Point to, Color color, double lineWidth) {
            this.items.add(new Line(from, to, color, lineWidth));
        }

        @Override
        public void addCurve(Point.Iterator ptIt, Color color, double lineWidth) {
            this.items.add(new Curve(ptIt, color, lineWidth));
        }

        @Override
        public Canvas getCanvas() {
            return this.canvas;
        }

        public void render() {
            double width = this.canvas.getWidth();
            double height = this.canvas.getHeight();

            final GraphicsContext gc = this.canvas.getGraphicsContext2D();

            gc.setTransform(new Affine());
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, width, height);

            Affine transform = new Affine();
            transform.appendTranslation(0, height);
            transform.appendScale(1, -1);
            transform.appendScale(width / (this.max.x() - this.min.x()),
                    height / (this.max.y() - this.min.y()));
            transform.appendTranslation(-this.min.x(), -this.min.y());

            gc.setTransform(transform);

            for (final PlotterItem item : this.items) {
                item.plot();
            }
        }
    }

    public enum PlotType {
        Cartesian, Polar
    }

    public enum ExpressionFormat {
        AOS, RPN
    }

    public enum AreaType {
        Rectangular,
        Trapezoidal
    }

    private String expressionString;        // input expression
    private String derivativeString;        // output derivative

    public MathPlot() {
        this.expressionString = null;
        this.derivativeString = null;
    }

    public void setExpression(String expr, ExpressionFormat format) {
        // stop if format is not AOS
        if (format != ExpressionFormat.AOS) {
            // TODO: implement RPN format
            return;
        }

        this.expressionString = expr;

        try {
            // parse expression string to AOS
            AOS parser = new AOS();
            AOS.Parts parsedExpression = parser.parse(expr);
            // simplify expression
            this.expressionString = simplify(parsedExpression);

            // Compute derivative
            String derivative = calculateDerivativeAOS(parsedExpression);
            // simplify derivative
            AOS.Parts parsedDerivative = parser.parse(derivative);
            this.derivativeString = simplify((parsedDerivative));
        } catch (Exception e) {
            throw new RuntimeException("Error while parsing expression: " + e.getMessage());
        }
    }

    public void plot(Canvas canvas, PlotType type) {
        final Plotter pf = new Plotter(canvas, new Point(-10, -10), new Point(10, 10));

        // parse expression strings
        AOS parser = new AOS();
        AOS.Parts derivativeTree;
        AOS.Parts expressionTree;
        try {
            derivativeTree = parser.parse(this.derivativeString);
            expressionTree = parser.parse(this.expressionString);
        } catch (Exception e) {
            pf.render();
            return;
        }

        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        switch (type) {
            // cartesian plot
            case Cartesian -> {
                double xMin = -10, xMax = 10;
                int steps = 1200;

                // coordinate axes
                pf.addLine(new Point(xMin, 0.0), new Point(xMax, 0.0), Color.GRAY, 0.1);
                pf.addLine(new Point(0.0, -10.0), new Point(0.0, 10.0), Color.GRAY, 0.1);

                // expression plot
                if (expressionTree != null) {
                    Point.Iterator expressionIterator = cartesianIterator(expressionTree, evaluator, xMin, xMax, steps);
                    pf.addCurve(expressionIterator, Color.BLUE, 0.2);
                }
                // derivative plot
                if (derivativeTree != null) {
                    Point.Iterator derivativeIterator = cartesianIterator(derivativeTree, evaluator, xMin, xMax, steps);
                    pf.addCurve(derivativeIterator, Color.RED, 0.2);
                }

                pf.render();
            }

            // polar plot
            case Polar -> {
                double thetaMin = 0.0;
                double thetaMax = 2 * Math.PI;
                int steps = 1200;

                // background grid
                int circleCount = 4;
                double rMax = 10;
                for (int i = 1; i <= circleCount; ++i) {
                    double r = (rMax * i) / circleCount;
                    pf.addCircle(new Point(0.0, 0.0), r, Color.LIGHTGRAY, 0.05);
                }
                int radialLines = 8;
                for (int i = 0; i < radialLines; ++i) {
                    double theta = (2 * Math.PI * i) / radialLines;
                    double x = rMax * Math.cos(theta);
                    double y = rMax * Math.sin(theta);
                    pf.addLine(new Point(0.0, 0.0), new Point(x, y), Color.LIGHTGRAY, 0.05);
                }

                // expression plot
                if (expressionTree != null) {
                    Point.Iterator expressionIterator = polarIterator(expressionTree, evaluator, thetaMin, thetaMax, steps);
                    pf.addCurve(expressionIterator, Color.BLUE, 0.2);
                }
                // derivative plot
                if (derivativeTree != null) {
                    Point.Iterator derivativeIterator = polarIterator(derivativeTree, evaluator, thetaMin, thetaMax, steps);
                    pf.addCurve(derivativeIterator, Color.DARKRED, 0.2);
                }

                pf.render();
            }
        }
    }

    // converts AOS expression to math
    private static class ExpressionEvaluator {
        private final AOS parser = new AOS();

        public double evaluate(AOS.Parts node, double x) throws Exception {
            if (node == null) return Double.NaN;

            String op = node.main;
            String left = node.left;
            String right = node.right;

            // evaluate leaf node
            if (left == null && right == null) {
                if ("x".equals(op)) return x;
                try {
                    return Integer.parseInt(op);
                } catch (NumberFormatException e) {
                    throw new Exception("Unknown value: " + op);
                }
            }

            // evaluate children nodes
            double L = 0, R = 0;
            AOS.Parts leftNode = null, rightNode = null;
            if (left != null) leftNode = parser.parse(left);
            if (right != null) rightNode = parser.parse(right);

            // evaluate operators
            switch (op) {
                case "+":
                    return evaluate(leftNode, x) + evaluate(rightNode, x);
                case "-":
                    return evaluate(leftNode, x) - evaluate(rightNode, x);
                case "*":
                    return evaluate(leftNode, x) * evaluate(rightNode, x);
                case "/":
                    return evaluate(leftNode, x) / evaluate(rightNode, x);

                case "^":
                    L = evaluate(leftNode, x);
                    R = evaluate(rightNode, x);
                    return Math.pow(L, R);

                case "sin":
                    return Math.sin(evaluate(leftNode, x));
                case "cos":
                    return Math.cos(evaluate(leftNode, x));
                case "exp":
                    return Math.exp(evaluate(leftNode, x));
                case "ln":
                    return Math.log(evaluate(leftNode, x));

                default:
                    throw new Exception("Unsupported operator: " + op);
            }
        }
    }

    // calculates points and breaks used to draw cartesian plot
    // breaks occur in non-continuous functions, the line skips this point
    private Point.Iterator cartesianIterator(AOS.Parts tree, ExpressionEvaluator eval,
                                             double xMin, double xMax, int steps) {
        return new Point.Iterator() {
            int idx = 0;
            boolean currentBreak = false;

            @Override
            public void reset() {
                idx = 0;
                currentBreak = false;
            }

            @Override
            public boolean hasNext() {
                return idx <= steps;
            }

            @Override
            public boolean hasBreak() {
                return currentBreak;
            }

            @Override
            public Point nextPoint() {
                double t = idx / (double) steps;
                double x = xMin + t * (xMax - xMin);
                double y;

                try {
                    y = eval.evaluate(tree, x);
                } catch (Exception ex) {
                    y = Double.NaN;
                }

                // mark current point as break point if function is not continuous here
                currentBreak = !Double.isFinite(y);
                idx++;

                return new Point(x, currentBreak ? 0.0 : y);
            }
        };
    }

    // calculates points and breaks used to draw polar coordinates
    // breaks occur in non-continuous functions, the line skips this point
    private Point.Iterator polarIterator(AOS.Parts tree, ExpressionEvaluator eval,
                                         double tMin, double tMax, int steps) {
        return new Point.Iterator() {
            int idx = 0;
            boolean currentBreak = false;

            @Override
            public void reset() {
                idx = 0;
                currentBreak = false;
            }

            @Override
            public boolean hasNext() {
                return idx <= steps;
            }

            @Override
            public boolean hasBreak() {
                return currentBreak;
            }

            @Override
            public Point nextPoint() {
                double t = idx / (double) steps;
                double theta = tMin + t * (tMax - tMin);

                double r;
                try {
                    r = eval.evaluate(tree, theta);
                } catch (Exception ex) {
                    r = Double.NaN;
                }

                double x = Double.NaN, y = Double.NaN;
                if (Double.isFinite(r)) {
                    x = r * Math.cos(theta);
                    y = r * Math.sin(theta);
                }

                // mark current point as break point if function is not continuous here
                currentBreak = !Double.isFinite(x) || !Double.isFinite(y);
                idx++;

                return new Point(currentBreak ? 0.0 : x, currentBreak ? 0.0 : y);
            }
        };
    }

    public double area(AreaType areaType) {
        // skip if expression is empty
        if (this.expressionString == null) {
            return 0.0;
        }

        try {
            // parse expression
            AOS parser = new AOS();
            AOS.Parts expr = parser.parse(this.expressionString);
            ExpressionEvaluator eval = new ExpressionEvaluator();

            // integration bounds (same as plot)
            final double xMin = -10.0;
            final double xMax =  10.0;
            final int steps = 10_000;
            final double dx = (xMax - xMin) / steps;

            double area = 0.0;

            switch (areaType) {
                // calculate area with rectangular method
                case Rectangular: {
                    for (int i = 0; i < steps; i++) {
                        double x = xMin + i * dx;
                        double y;

                        try {
                            y = eval.evaluate(expr, x);
                        } catch (Exception ex) {
                            y = 0.0;
                        }

                        area += y * dx;
                    }
                    break;
                }

                // calculate area with trapezoidal method
                case Trapezoidal: {
                    for (int i = 0; i < steps; i++) {
                        double x1 = xMin + i * dx;
                        double x2 = x1 + dx;

                        double y1, y2;

                        try { y1 = eval.evaluate(expr, x1); }
                        catch (Exception ex) { y1 = 0.0; }

                        try { y2 = eval.evaluate(expr, x2); }
                        catch (Exception ex) { y2 = 0.0; }

                        area += 0.5 * (y1 + y2) * dx;
                    }
                    break;
                }
            }

            return area;

        } catch (Exception e) {
            return 0.0;
        }
    }

    public List<String> print(ExpressionFormat format) {
        List<String> res = new ArrayList<>();

        // return empty if format is not AOS
        if (format != ExpressionFormat.AOS) {
            // TODO: implement RPN format
            return new ArrayList<>();
        }

        // add original expression
        if (this.expressionString != null) {
            res.add(this.expressionString);
        }
        // add derivative
        if (this.derivativeString != null) {
            res.add(this.derivativeString);
        }

        return res;
    }

    // calculate derivative
    // public for testing
    // TODO: calculate derivative in RPN format
    public String calculateDerivativeAOS(AOS.Parts node) throws Exception {
        if (node == null) return "";

        String op = node.main;
        String leftStr = node.left;
        String rightStr = node.right;

        // x = 1, constants = 0
        if (leftStr == null && rightStr == null) {
            if ("x".equalsIgnoreCase(op)) return "1";
            else return "0";
        }

        AOS parser = new AOS();

        switch (op) {
            case "+":
            case "-": {
                AOS.Parts leftNode = parser.parse(leftStr);
                AOS.Parts rightNode = parser.parse(rightStr);
                return "(" + calculateDerivativeAOS(leftNode) + " " + op + " " + calculateDerivativeAOS(rightNode) + ")";
            }
            case "*": {
                AOS.Parts leftNode = parser.parse(leftStr);
                AOS.Parts rightNode = parser.parse(rightStr);
                // product rule f'g + fg'
                return "((" + calculateDerivativeAOS(leftNode) + " * " + rightStr + ") + (" + leftStr + " * " + calculateDerivativeAOS(rightNode) + "))";
            }
            case "/": {
                AOS.Parts leftNode = parser.parse(leftStr);
                AOS.Parts rightNode = parser.parse(rightStr);
                // quotient rule (f'g - fg') / g^2
                return "(((" + calculateDerivativeAOS(leftNode) + " * " + rightStr + ") - (" + leftStr + " * " + calculateDerivativeAOS(rightNode) + ")) / (" + rightStr + "^2))";
            }
            case "^": {
                try {
                    int exp = Integer.parseInt(rightStr);
                    return "(" + rightStr + " * " + leftStr + "^" + (exp - 1) + ")";
                } catch (NumberFormatException e) {
                    return "d(" + leftStr + "^" + rightStr + ")";
                }
            }
            default: {
                AOS.Parts argNode = parser.parse(leftStr);
                switch (op) {
                    case "sin":
                        return "(cos(" + leftStr + ") * " + calculateDerivativeAOS(argNode) + ")";
                    case "cos":
                        return "(-sin(" + leftStr + ") * " + calculateDerivativeAOS(argNode) + ")";
                    case "exp":
                        return "(exp(" + leftStr + ") * " + calculateDerivativeAOS(argNode) + ")";
                    case "ln":
                        return "(" + calculateDerivativeAOS(argNode) + " / " + leftStr + ")";
                    default:
                        return "d(" + op + ")";
                }
            }
        }
    }

    private String simplify(AOS.Parts node) throws Exception {
        if (node == null)
            return "";

        // leaf node
        if (node.left == null && node.right == null)
            return node.main;

        AOS parser = new AOS();

        // recursively simplify left/right
        String leftS = node.left != null ? simplify(parser.parse(node.left)) : null;
        String rightS = node.right != null ? simplify(parser.parse(node.right)) : null;

        String op = node.main;

        // safe numeric parsing
        Integer L = tryParseInteger(leftS);
        Integer R = tryParseInteger(rightS);

        boolean leftIsNum = L != null;
        boolean rightIsNum = R != null;

        // binary operations
        switch (op) {
            case "+":
                if (leftIsNum && rightIsNum) return String.valueOf(L + R);
                if (leftIsNum && L == 0) return rightS;
                if (rightIsNum && R == 0) return leftS;
                return "(" + leftS + " + " + rightS + ")";

            case "-":
                if (leftIsNum && rightIsNum) return String.valueOf(L - R);
                if (rightIsNum && R == 0) return leftS;
                return "(" + leftS + " - " + rightS + ")";

            case "*":
                if (leftIsNum && rightIsNum) return String.valueOf(L * R);
                if (leftIsNum && L == 1) return rightS;
                if (rightIsNum && R == 1) return leftS;
                if ((leftIsNum && L == 0) || (rightIsNum && R == 0)) return "0";
                return "(" + leftS + " * " + rightS + ")";

            case "/":
                if (leftIsNum && rightIsNum) return String.valueOf(L / R);
                if (rightIsNum && R == 1) return leftS;
                return "(" + leftS + " / " + rightS + ")";

            case "^":
                if (rightIsNum) {
                    if (R == 0) return "1";
                    if (R == 1) return leftS;
                    if (leftIsNum) return String.valueOf(Math.pow(L, R));
                }
                return "(" + leftS + "^" + rightS + ")";
        }

        // other operations
        switch (op) {
            case "sin":
                if (leftIsNum) return String.valueOf(Math.sin(L));
                return "sin(" + leftS + ")";

            case "cos":
                if (leftIsNum) return String.valueOf(Math.cos(L));
                return "cos(" + leftS + ")";

            case "exp":
                if (leftIsNum) return String.valueOf(Math.exp(L));
                return "exp(" + leftS + ")";

            case "ln":
                if (leftIsNum) return String.valueOf(Math.log(L));
                return "ln(" + leftS + ")";
        }

        return "(" + op + "(" + leftS + (rightS != null ? "," + rightS : "") + "))";
    }

    private Integer tryParseInteger(String s) {
        if (s == null) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
