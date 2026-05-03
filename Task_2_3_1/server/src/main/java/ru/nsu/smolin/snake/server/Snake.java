package ru.nsu.smolin.snake.server;

import ru.nsu.smolin.snake.protocol.Direction;
import ru.nsu.smolin.snake.protocol.Point;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Серверное состояние одной змейки: тело, направление, очки и статус.
 */
public class Snake {
    public final String playerId;
    public final String playerName;
    public final Deque<Point> segments = new ArrayDeque<>();  // тело змейки; голова — первый элемент, хвост — последний
    public Direction direction = Direction.RIGHT;             // направление, применённое в последнем тике
    public volatile Direction pendingDir = Direction.RIGHT;  // направление от клиента; volatile т.к. пишется из потока клиента, читается из game-loop
    public boolean alive = true;
    public int score = 0;
    public int respawnTicks = 0;

    public Snake(String playerId, String playerName, int headX, int headY) {
        this.playerId = playerId;
        this.playerName = playerName;
        segments.addFirst(new Point(headX, headY));
        segments.addLast(new Point(headX - 1, headY));
        segments.addLast(new Point(headX - 2, headY));
    }

    public Point head() {
        return segments.peekFirst();
    }
}
