package ru.nsu.smolin;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Eval const.
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Const extends Expression {
    private int value;

    @Override
    public int eval(Map<String, Integer> variables) {
        return value;
    }

    @Override
    public Expression derivative(String var) {
        return new Const(0);
    }

    @Override
    public String print() {
        return Integer.toString(value);
    }
}
