package ru.nsu.smolin.snake.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Снимок игрового состояния, рассылаемый сервером всем клиентам каждый тик.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameStateDto {
    private List<PlayerDto> players;
    private List<FoodDto> food;
    private long tick;
}
