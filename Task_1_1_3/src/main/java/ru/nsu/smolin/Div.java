package ru.nsu.smolin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Div extends Expression {
    private Expression left;
    private Expression right;

    /**
     * Division with division by zero protection
     *
     * @param variables input variobles
     * @return a/b
     */
    @Override
    public int eval(Map<String, Integer> variables) {
        if(right.eval(variables) == 0) {
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
    public String print() {
        return "(" + left.print() + "/" + right.print() + ")";
    }
}
