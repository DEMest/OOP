package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Test of mul expression.
 *
 */
class MulTest {
    private Expression expression =
            new Mul(
                    new Const(5),
                    new Variable("x")
            );

    @Test
    void print_eval() {
        assertEquals("(5*x)", expression.print());
        assertEquals(10, expression.eval(Map.of("x", 2)));
    }

    @Test
    void print_derivative() {
        Expression de = expression.derivative("x");
        assertEquals("((0*x)+(5*1))", de.print());
        assertEquals(5, de.eval(Map.of("x", 10)));
    }
}
