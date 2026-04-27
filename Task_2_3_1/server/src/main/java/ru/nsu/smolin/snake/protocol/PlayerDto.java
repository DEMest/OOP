package ru.nsu.smolin.snake.protocol;

import java.util.List;

/**
 * Данные одного игрока, передаваемые клиенту внутри {@link GameStateDto}.
 */
public class PlayerDto {
    public String id;
    public String name;
    public int score;
    public boolean alive;
    public List<Point> segments;
    public Direction direction;
}
