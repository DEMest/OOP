package ru.nsu.smolin.snake.server;

import com.google.gson.Gson;
import ru.nsu.smolin.snake.protocol.Msg;

import java.io.*;
import java.net.Socket;

/**
 * Обслуживает одного подключённого клиента в отдельном потоке:
 * читает входящие JSON-сообщения построчно и передаёт их в {@link GameRoom}.
 * Метод {@link #send} вызывается из потока игрового цикла, поэтому он синхронизирован.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final GameRoom room;
    private final Gson gson = new Gson();
    private volatile PrintWriter out;  // volatile т.к. инициализируется в run(), а читается из send() другого потока
    private String playerId;

    public ClientHandler(Socket socket, GameRoom room) {
        this.socket = socket;
        this.room = room;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), false);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                handleMessage(gson.fromJson(line, Msg.class));
            }
        } catch (IOException e) {
            System.out.println("Client " + playerId + " disconnected: " + e.getMessage());
        } finally {
            if (playerId != null) room.removePlayer(playerId);
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private void handleMessage(Msg msg) {
        if (msg == null || msg.type == null) return;
        switch (msg.type) {
            case "JOIN" -> {
                String name = (msg.name != null && !msg.name.isBlank()) ? msg.name : "Player";
                playerId = room.addPlayer(name, this);
                Msg welcome = new Msg();
                welcome.type = "WELCOME";
                welcome.playerId = playerId;
                welcome.gridW = room.getW();
                welcome.gridH = room.getH();
                send(gson.toJson(welcome) + "\n");
                System.out.println("Player joined: " + name + " (" + playerId + ")");
            }
            case "DIR" -> {
                if (playerId != null && msg.dir != null) {
                    room.setDirection(playerId, msg.dir);
                }
            }
        }
    }

    public synchronized void send(String json) {
        if (out != null) {
            out.print(json);
            out.flush();
        }
    }
}
