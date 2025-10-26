package ru.nsu.smolin;

import ru.nsu.smolin.impl.Add;
import ru.nsu.smolin.impl.Const;
import ru.nsu.smolin.impl.Mul;
import ru.nsu.smolin.impl.Variable;
import java.util.Map;

/**
 * Main class for example usage of expr programm.
 *
 */
public class Main {
    /**
     * Example usage.
     *
     * @param args just an args
     */
    public static void main(String[] args) {
        Expression e = new Add(
                new Const(3),
                new Mul(new Const(2), new Variable("x"))
        );

        System.out.println("Expression: " + e.toString());
        Expression de = e.derivative("x");
        System.out.println("Derivative: " + de.toString());

        Map<String, Integer> vars = Map.of("x", 10);
        System.out.println("Eval of x=" + vars.get("x") + ": " + e.eval(vars));
    }
}

