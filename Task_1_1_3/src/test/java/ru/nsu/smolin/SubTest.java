package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.nsu.smolin.impl.Const;
import ru.nsu.smolin.impl.Sub;
import ru.nsu.smolin.impl.Variable;

/**
 * Test of sub expression.
 *
 */
class SubTest {
    private static Expression expression;

    @BeforeAll
    static void init() {
        expression =
                new Sub(
                        new Const(5),
                        new Variable("x")
                );
    }

    @Test
    void print_eval() {
        assertEquals("(5-x)", expression.toString());
        assertEquals(2, expression.eval(Map.of("x", 3)));
    }

    @Test
    void print_derivative() {
        Expression de = expression.derivative("x");
        assertEquals("(0-1)", de.toString());
        assertEquals(-1, de.eval(Map.of()));
    }
}
