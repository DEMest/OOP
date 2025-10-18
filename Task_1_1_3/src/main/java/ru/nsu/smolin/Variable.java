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
public class Variable extends Expression {
    private String name;

    @Override
    public int eval(Map<String, Integer> variables) {
        if (!variables.containsKey(name))
            throw new IllegalArgumentException(
                    "Variable" + name + "not defined");
        return variables.get(name);
    }

    @Override
    public Expression derivative(String var) {
        return new Const(name.equals(var) ? 1 : 0);
    }

    @Override
    public String print() {
        return name;
    }
}
