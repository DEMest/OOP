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
