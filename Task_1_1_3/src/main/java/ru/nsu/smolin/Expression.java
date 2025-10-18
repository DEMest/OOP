package ru.nsu.smolin;

import ru.nsu.smolin.impl.*;

import java.util.Map;

/**
 * Абстрактный класс математических выражений.
 *
 * <h2>Для пользователей:</h2>
 * Готовые реализации - {@link Const}, {@link Variable},
 * {@link Add}, {@link Sub}, {@link Mul}, {@link Div} для создания дерева выражений:
 * <pre>{@code
 * Expression expr = new Add(new Const(5), new Variable("x")); // выражение вида (5+x)
 * int result = expr.eval(Map.of("x", 3)); // result = 8
 * Expression d = expr.derivative("x");   // производная: (0+1)
 * }</pre>
 *
 * Для выражения типа {@link Div} в методе {@code eval} есть исключение
 * от деления на ноль - ArithmeticException.
 *
 * <h2>Для разработчиков:</h2>
 * Чтобы добавить новую реализацию или операцию, необходимо:
 * <ol>
 *   <li>Создать новый класс и унаследовать его от {@code Expression};</li>
 *   <li>Реализовать метод {@link #eval(Map)} — как вычисляется это выражение;</li>
 *   <li>Реализовать метод {@link #derivative(String)} — правило дифференциации;</li>
 *   <li>Реализовать метод {@code toString()}</li>
 * </ol>
 */
public interface Expression {
    /**
     * just a eval func.
     *
     * @param variables which we want to evaluate
     * @return
     */
    int eval(Map<String, Integer> variables);

    /**
     * just a derivative func (developer should implement it
     * by formulas like x*y=(x'*y)+(x*y').
     *
     * @param var by which variable expr derivate.
     * @return derivative expression.
     */
    Expression derivative(String var);

}
