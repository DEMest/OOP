package ru.nsu.smolin.impl;

import java.util.Map;

import lombok.*;
import ru.nsu.smolin.Expression;

/**
 * Eval const.
 *
 */
@Data
public class Const implements Expression {
    private final int value;

    @Override
    public int eval(Map<String, Integer> variables) {
        return value;
    }

    @Override
    public Expression derivative(String var) {
        return new Const(0);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
