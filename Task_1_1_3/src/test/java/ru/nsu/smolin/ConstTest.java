package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Test of const.
 *
 */
class ConstTest {
    private Expression expression = new Const(5);

    @Test
    void print_eval() {
        assertEquals("5", expression.print());
        assertEquals(5, expression.eval(Map.of()));
    }

    @Test
    void print_derivative() {
        Expression de = expression.derivative("x");
        assertEquals("0", de.print());
        assertEquals(0, de.eval(Map.of()));
    }
}
