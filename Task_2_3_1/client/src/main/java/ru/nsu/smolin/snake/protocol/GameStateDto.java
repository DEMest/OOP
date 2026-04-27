package ru.nsu.smolin.snake.protocol;

import java.util.List;

/**
 * Снимок игрового состояния, рассылаемый сервером всем клиентам каждый тик.
 */
public class GameStateDto {
    public List<PlayerDto> players;
    public List<FoodDto> food;
    public long tick;
}
