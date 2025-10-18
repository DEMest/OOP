package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.nsu.smolin.impl.Const;
import ru.nsu.smolin.impl.Div;
import ru.nsu.smolin.impl.Variable;

/**
 * Test of div expression.
 *
 */
class DivTest {
    private static Expression expression;

    @BeforeAll
    static void init() {
        expression =
                new Div(
                        new Const(6),
                        new Variable("x")
                );
    }

    @Test
    void print_eval() {
        assertEquals("(6/x)", expression.toString());
        assertEquals(3, expression.eval(Map.of("x", 2)));
    }

    @Test
    void print_derivative() {
        Expression de = expression.derivative("x");
        assertEquals("(((0*x)-(6*1))/(x*x))", de.toString());
        assertEquals(-1.0, de.eval(Map.of("x", 2)));
    }

    @Test
    void division_by_zero() {
        assertThrows(ArithmeticException.class,
                () -> expression.eval(Map.of("x", 0)));
    }
}
