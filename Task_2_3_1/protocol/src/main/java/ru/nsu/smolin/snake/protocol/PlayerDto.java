package ru.nsu.smolin.snake.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Данные одного игрока, передаваемые клиенту внутри {@link GameStateDto}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {
    private String id;
    private String name;
    private int score;
    private boolean alive;
    private List<Point> segments;
    private Direction direction;
}
