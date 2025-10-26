package ru.nsu.smolin.impl;

import java.util.Map;

import lombok.*;
import ru.nsu.smolin.Expression;

/**
 * Mul left and right Expression.
 *
 */
@Data
public class Mul implements Expression {
    private final Expression left;
    private final Expression right;

    @Override
    public int eval(Map<String, Integer> variables) {
        return left.eval(variables) * right.eval(variables);
    }

    /**
     * Func of derivate for mul.
     *
     * @param var input var
     * @return (f*g)' = f'*g + f*g'
     */
    @Override
    public Expression derivative(String var) {
        return new Add(
                new Mul(left.derivative(var), right),
                new Mul(left, right.derivative(var))
        );
    }

    @Override
    public String toString() {
        return
                "(" + left +
                        "*" + right +
                        ")";
    }
}
