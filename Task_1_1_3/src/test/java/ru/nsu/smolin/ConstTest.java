package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.nsu.smolin.impl.Const;

/**
 * Test of const.
 *
 */
class ConstTest {
    private static Expression expression;

    @BeforeAll
    static void init() {
        expression = new Const(5);
    }

    @Test
    void print_eval() {
        assertEquals("5", expression.toString());
        assertEquals(5, expression.eval(Map.of()));
    }

    @Test
    void print_derivative() {
        Expression de = expression.derivative("x");
        assertEquals("0", de.toString());
        assertEquals(0, de.eval(Map.of()));
    }
}
