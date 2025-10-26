package ru.nsu.smolin.impl;

import java.util.Map;

import lombok.*;
import ru.nsu.smolin.Expression;

/**
 * Variable.
 *
 */
@Data
public class Variable implements Expression {
    private final String name;

    @Override
    public int eval(Map<String, Integer> variables) {
        if (!variables.containsKey(name)) {
            throw new IllegalArgumentException(
                    "Variable" + name + "not defined");
        }
        return variables.get(name);
    }

    @Override
    public Expression derivative(String var) {
        return new Const(name.equals(var) ? 1 : 0);
    }

    @Override
    public String toString() {
        return name;
    }
}
