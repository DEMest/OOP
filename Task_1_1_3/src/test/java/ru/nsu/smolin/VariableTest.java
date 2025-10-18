package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Test of variable.
 *
 */
class VariableTest {
    private Expression e = new Variable("x");

    @Test
    void print_eval_defined() {
        assertEquals("x", e.print());
        assertEquals(10, e.eval(Map.of("x", 10)));
    }

    @Test
    void eval_undefined() {
        assertThrows(IllegalArgumentException.class,
                () -> e.eval(Map.of("y", 10)));
    }

    @Test
    void print_derivative() {
        assertEquals("1", e.derivative("x").print());
    }
}
