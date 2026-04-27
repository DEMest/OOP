package ru.nsu.smolin.snake.client;

import com.google.gson.Gson;
import ru.nsu.smolin.snake.protocol.Msg;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Управляет TCP-соединением с сервером: отправляет JSON-сообщения
 * и слушает входящие в фоновом потоке-демоне {@code server-reader}.
 */
public class ServerConnection {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final Gson gson = new Gson();

    public ServerConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), false);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void send(Msg msg) {
        out.print(gson.toJson(msg) + "\n");
        out.flush();
    }

    public void startListening(Consumer<Msg> onMessage, Runnable onDisconnect) {
        Thread reader = new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    Msg msg = gson.fromJson(line, Msg.class);
                    if (msg != null) onMessage.accept(msg);
                }
            } catch (IOException ignored) {}
            onDisconnect.run();
        }, "server-reader");
        reader.setDaemon(true);
        reader.start();
    }

    public void close() {
        try { socket.close(); } catch (IOException ignored) {}
    }
}
