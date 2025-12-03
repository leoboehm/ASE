package MathPlot;

import MathPlot.Calculators.Node;
import MathPlot.Calculators.Operators.*;
import MathPlot.Calculators.Operators.BinaryOperations.*;
import MathPlot.Parsers.AOS;
import MathPlot.MathPlot.ExpressionFormat;

public class FormatTranslator {
    public static Node fromAOS(String expression) throws Exception {
        return buildAOS(expression);
    }
    public static Node fromRPN(String expression) throws Exception {
        throw new Exception("Format is not implemented");
    }
    public static String toString(Node expression, ExpressionFormat format) throws Exception {
        if (format == ExpressionFormat.AOS){
            return buildStringFromAOS(expression);
        } else {
            return buildStringFromRPN(expression);
        }
    }

    private static Node buildAOS(String expression) throws Exception {
        AOS parser = new AOS();
        AOS.Parts parts = parser.parse(expression);

        try {
            double val = Double.parseDouble(parts.main);
            return new Constant(val);
        } catch (NumberFormatException e) {
            // continue
        }

        if (parts.main.equalsIgnoreCase("x")) {
            return new Variable();
        }

        String op = parts.main;

        if (op.equals("sin")) return new Sin(buildAOS(parts.left));
        if (op.equals("cos")) return new Cos(buildAOS(parts.left));

        Node left = buildAOS(parts.left);
        Node right = buildAOS(parts.right);
        return switch (op) {
            case "+" -> new Add(left, right);
            case "-" -> new Sub(left, right);
            case "*" -> new Mult(left, right);
            case "/" -> new Div(left, right);
            case "^" -> new Power(left, right);
            default -> throw new Exception("Unknown operator: " + op);
        };
    }

    private static String buildStringFromAOS(Node expr) {
        switch (expr) {
            case Variable _ -> {
                return "x";
            }
            case Constant constantNode -> {
                return String.valueOf(constantNode.eval(0));
            }
            case Sin sinNode -> {
                return "sin(" + buildStringFromAOS(sinNode.child) + ")";
            }
            case Cos cosNode -> {
                return "cos(" + buildStringFromAOS(cosNode.child) + ")";
            }

            case BinaryOperation binaryNode -> {
                String leftAOS = buildStringFromAOS(binaryNode.left);
                String rightAOS = buildStringFromAOS(binaryNode.right);
                String operator = "";

                switch (expr) {
                    case Add _ -> operator = " + ";
                    case Sub _ -> operator = " - ";
                    case Mult _ -> operator = " * ";
                    case Div _ -> operator = " / ";
                    case Power _ -> operator = " ^ ";
                    default -> {
                    }
                }

                return "(" + leftAOS + operator + rightAOS + ")";
            }
            default -> {
                return "";
            }
        }
    }

    private static String buildStringFromRPN(Node expression) throws Exception {
        throw new Exception("Format is not implemented");
    }
}
