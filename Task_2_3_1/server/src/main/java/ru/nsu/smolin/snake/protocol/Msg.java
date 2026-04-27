package ru.nsu.smolin.snake.protocol;

/**
 * Универсальный конверт для всех сообщений протокола клиент↔сервер.
 * Поле {@code type} определяет, какие поля заполнены:
 * JOIN, DIR — клиент → сервер; WELCOME, STATE — сервер → клиент.
 */
public class Msg {
    public String type;    // JOIN | DIR | WELCOME | STATE
    // JOIN
    public String name;
    // DIR
    public Direction dir;
    // WELCOME
    public String playerId;
    public int gridW, gridH;
    // STATE
    public GameStateDto state;
}
