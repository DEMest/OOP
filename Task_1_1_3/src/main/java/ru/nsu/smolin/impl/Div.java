package ru.nsu.smolin.impl;

import java.util.Map;

import lombok.*;
import ru.nsu.smolin.Expression;

/**
 * Div left and right Expression.
 *
 */
@Data
public class Div implements Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Division with division by zero protection.
     *
     * @param variables input variobles
     * @return a/b
     */
    @Override
    public int eval(Map<String, Integer> variables) {
        if (right.eval(variables) == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return left.eval(variables) / right.eval(variables);
    }

    /**
     * Func of derivative of div.
     *
     * @param var input var
     * @return (f/g)' = (f'*g - f*g') / (g*g)
     */
    @Override
    public Expression derivative(String var) {
        return new Div(
                new Sub(
                        new Mul(left.derivative(var), right),
                        new Mul(left, right.derivative(var))
                ),
                new Mul(right, right)
        );
    }

    @Override
    public String toString() {
        return
                "(" + left +
                        "/" + right +
                        ")";
    }
}
