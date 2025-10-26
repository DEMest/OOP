package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.nsu.smolin.impl.Variable;

/**
 * Test of variable.
 *
 */
class VariableTest {
    private static Expression expression;

    @BeforeAll
    static void init() {
        expression = new Variable("x");
    }

    @Test
    void print_eval_defined() {
        assertEquals("x", expression.toString());
        assertEquals(10, expression.eval(Map.of("x", 10)));
    }

    @Test
    void eval_undefined() {
        assertThrows(IllegalArgumentException.class,
                () -> expression.eval(Map.of("y", 10)));
    }

    @Test
    void print_derivative() {
        assertEquals("1", expression.derivative("x").toString());
    }
}
