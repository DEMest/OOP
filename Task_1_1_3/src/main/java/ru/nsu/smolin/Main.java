package ru.nsu.smolin;

import java.util.Map;

/**
 * Example usage.
 *
 */
public class Main {
    /**
     * Example usage.
     */
    public static void main(String[] args) {
        Expression e = new Add(
                new Const(3),
                new Mul(new Const(2), new Variable("x"))
        );

        System.out.println("Expression: " + e.print());
        Expression de = e.derivative("x");
        System.out.println("Derivative: " + de.print());

        Map<String, Integer> vars = Map.of("x", 10);
        System.out.println("Eval: " + e.eval(vars));
    }
}

