package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Test of div expression.
 *
 */
class DivTest {
    private Expression expression =
            new Div(
                    new Const(6),
                    new Variable("x")
            );

    @Test
    void print_eval() {
        assertEquals("(6/x)", expression.print());
        assertEquals(3, expression.eval(Map.of("x", 2)));
    }

    @Test
    void print_derivative() {
        Expression de = expression.derivative("x");
        assertEquals("(((0*x)-(6*1))/(x*x))", de.print());
        assertEquals(-1.0, de.eval(Map.of("x", 2)));
    }

    @Test
    void division_by_zero() {
        assertThrows(ArithmeticException.class,
                () -> expression.eval(Map.of("x", 0)));
    }
}
