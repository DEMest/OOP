package ru.nsu.smolin.snake.protocol;

import lombok.Getter;
import lombok.Setter;

/**
 * Универсальный конверт для всех сообщений протокола клиент↔сервер.
 * Поле {@code type} определяет, какие поля заполнены:
 * JOIN, DIR — клиент → сервер; WELCOME, STATE — сервер → клиент.
 */
@Getter
@Setter
public class Msg {
    private String type;
    private String name;
    private Direction dir;
    private String playerId;
    private int gridW;
    private int gridH;
    private GameStateDto state;
}
