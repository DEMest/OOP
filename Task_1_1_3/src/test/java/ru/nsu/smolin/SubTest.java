package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Test of sub expression.
 *
 */
class SubTest {
    private Expression expression =
            new Sub(
                    new Const(5),
                    new Variable("x")
            );

    @Test
    void print_eval() {
        assertEquals("(5-x)", expression.print());
        assertEquals(2, expression.eval(Map.of("x", 3)));
    }

    @Test
    void print_derivative() {
        Expression de = expression.derivative("x");
        assertEquals("(0-1)", de.print());
        assertEquals(-1, de.eval(Map.of()));
    }
}
