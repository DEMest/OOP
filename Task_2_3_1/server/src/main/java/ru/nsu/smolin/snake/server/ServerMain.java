package ru.nsu.smolin.snake.server;

/**
 * Точка входа сервера. Читает конфигурацию из переменных окружения
 * и запускает {@link GameServer}.
 */
public class ServerMain {
    public static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "6767"));
    public static final int GRID_W = 100;
    public static final int GRID_H = 100;

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Snake Server — grid " + GRID_W + "x" + GRID_H + ", port " + PORT);
        new GameServer(PORT, GRID_W, GRID_H).start();
    }
}
