package ru.nsu.smolin;

import java.util.Map;

/**
 * Abstract base class for math expressions.
 *
 */
public abstract class Expression {

    public abstract  int eval(Map<String, Integer> variables);

    public abstract Expression derivative(String var);

    public abstract String print();
}
