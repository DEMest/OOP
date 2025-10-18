package ru.nsu.smolin.impl;

import java.util.Map;

import lombok.*;
import ru.nsu.smolin.Expression;

/**
 * Sub left and right Expression.
 *
 */
@Data
public class Sub implements Expression {
    private final Expression left;
    private final Expression right;

    @Override
    public int eval(Map<String, Integer> variables) {
        return left.eval(variables) - right.eval(variables);
    }

    @Override
    public Expression derivative(String var) {
        return new Sub(left.derivative(var), right.derivative(var));
    }

    @Override
    public String toString() {
        return
                "(" + left +
                        "-" + right +
                        ")";
    }
}
