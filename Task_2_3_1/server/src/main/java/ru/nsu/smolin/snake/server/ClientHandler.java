package ru.nsu.smolin.snake.server;

import com.google.gson.Gson;
import ru.nsu.smolin.snake.protocol.GameStateDto;
import ru.nsu.smolin.snake.protocol.Msg;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        if (msg == null || msg.getType() == null) return;
        switch (msg.getType()) {
            case "JOIN" -> {
                String name = (msg.getName() != null && !msg.getName().isBlank()) ? msg.getName() : "Player";
                playerId = room.addPlayer(name, this);
                Msg welcome = new Msg();
                welcome.setType("WELCOME");
                welcome.setPlayerId(playerId);
                welcome.setGridW(room.getW());
                welcome.setGridH(room.getH());
                send(welcome);
                System.out.println("Player joined: " + name + " (" + playerId + ")");
            }
            case "DIR" -> {
                if (playerId != null && msg.getDir() != null) {
                    room.setDirection(playerId, msg.getDir());
                }
            }
        }
    }

    void send(GameStateDto state) {
        Msg msg = new Msg();
        msg.setType("STATE");
        msg.setState(state);
        send(msg);
    }

    void send(Msg msg) {
        sendRaw(gson.toJson(msg) + "\n");
    }

    private synchronized void sendRaw(String json) {
        if (out != null) {
            out.print(json);
            out.flush();
        }
    }
}
