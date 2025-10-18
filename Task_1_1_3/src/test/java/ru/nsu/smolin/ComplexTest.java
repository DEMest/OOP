package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Test of complex expression.
 *
 */
class ComplexTest {
    private Expression expression =
            new Add(
                    new Const(5),
                    new Mul(
                            new Const(2),
                            new Variable("x")
                    )
            );

    @Test
    void print_eval() {
        assertEquals("(5+(2*x))", expression.print());
        assertEquals(11, expression.eval(Map.of("x", 3)));
    }

    @Test
    void print_derivative() {
        Expression de = expression.derivative("x");
        assertEquals("(0+((0*x)+(2*1)))", de.print());
        assertEquals(2, de.eval(Map.of("x", 3)));
    }
}
