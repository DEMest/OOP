package ru.nsu.smolin.snake.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP-сервер: слушает входящие подключения и для каждого
 * создаёт отдельный поток с {@link ClientHandler}.
 */
public class GameServer {
    private final int port;
    private final GameRoom room;

    public GameServer(int port, int gridW, int gridH) {
        this.port = port;
        this.room = new GameRoom(gridW, gridH);
    }

    public void start() throws IOException {
        room.startGameLoop();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Snake server listening on port " + port);
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Connection from " + client.getRemoteSocketAddress());
                new Thread(new ClientHandler(client, room)).start();
            }
        }
    }
}
