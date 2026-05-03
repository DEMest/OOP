package ru.nsu.smolin.snake.protocol;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Координата клетки на игровом поле.
 */
@Getter
@Setter
@NoArgsConstructor
public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
