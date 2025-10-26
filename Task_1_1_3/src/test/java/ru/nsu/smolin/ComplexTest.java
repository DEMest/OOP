package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.nsu.smolin.impl.Add;
import ru.nsu.smolin.impl.Const;
import ru.nsu.smolin.impl.Mul;
import ru.nsu.smolin.impl.Variable;

/**
 * Test of complex expression.
 *
 */
class ComplexTest {
    private static Expression expression;

    @BeforeAll
    static void init() {
        expression = new Add(
                new Const(5),
                new Mul(
                        new Const(2),
                        new Variable("x")
                )
        );
    }

    @Test
    void print_eval() {
        assertEquals("(5+(2*x))", expression.toString());
        assertEquals(11, expression.eval(Map.of("x", 3)));
    }

    @Test
    void print_derivative() {
        Expression de = expression.derivative("x");
        assertEquals("(0+((0*x)+(2*1)))", de.toString());
        assertEquals(2, de.eval(Map.of("x", 3)));
    }
}
