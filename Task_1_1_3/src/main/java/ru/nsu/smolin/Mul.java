package ru.nsu.smolin;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Mul left and right Expression.
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mul extends Expression {
    private Expression left;
    private Expression right;

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
    public String print() {
        return "(" + left.print() + "*" + right.print() + ")";
    }
}
