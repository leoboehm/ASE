package MathPlot;

import MathPlot.Calculators.Node;
import MathPlot.Calculators.Operators.*;
import MathPlot.Calculators.Operators.BinaryOperations.*;
import MathPlot.MathPlot.ExpressionFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FormatTranslatorTest {
    // test AOS parsing & toString()
    @Test
    void testExpressionFormatting() throws Exception {
        Node n = FormatTranslator.fromAOS("(3 * (x + 2))");
        String s = FormatTranslator.toString(n, ExpressionFormat.AOS);
        assertEquals("(3.0 * (x + 2.0))", s);
    }

    // test AOS parser error
    @Test
    void testUnknownOperator() {
        assertThrows(Exception.class, () -> FormatTranslator.fromAOS("(3 ? 4)"));
    }
    @Test
    void testWeirdExpression() {
        assertThrows(Exception.class, () -> FormatTranslator.fromAOS("(3 + )"));
    }

    // test RPN throws error
    @Test
    void testFromRPN() {
        assertThrows(Exception.class, () -> FormatTranslator.fromRPN("3 x +"));
    }
    @Test
    void testToStringRPN() {
        Node n = new Add(new Constant(1), new Constant(2));
        assertThrows(Exception.class, () -> FormatTranslator.toString(n, ExpressionFormat.RPN));
    }
}