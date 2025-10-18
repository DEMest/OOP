package ru.nsu.smolin;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Add left and right Expression.
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Add extends Expression {
    private Expression left;
    private Expression right;

    @Override
    public int eval(Map<String, Integer> variables) {
        return left.eval(variables) + right.eval(variables);
    }

    @Override
    public Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }

    @Override
    public String print() {
        return "(" + left.print() + "+" + right.print() + ")";
    }
}
