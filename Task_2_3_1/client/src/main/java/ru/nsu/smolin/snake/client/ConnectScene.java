package ru.nsu.smolin.snake.client;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Экран подключения: форма ввода хоста, порта и имени игрока.
 * При успешном подключении переходит к {@link GameScene}.
 */
public class ConnectScene {
    private final Stage stage;

    public ConnectScene(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        TextField hostField = new TextField("37.195.117.62");
        TextField portField = new TextField("6769");
        TextField nameField = new TextField("Player");
        Button connectBtn = new Button("Connect");
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");

        connectBtn.setDefaultButton(true);
        connectBtn.setOnAction(e -> tryConnect(hostField.getText().trim(),
            portField.getText().trim(), nameField.getText().trim(), statusLabel));

        VBox box = new VBox(8,
            new Label("Server host:"), hostField,
            new Label("Port:"), portField,
            new Label("Your name:"), nameField,
            connectBtn, statusLabel
        );
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: white;");
        box.getChildren().stream()
            .filter(n -> n instanceof Label)
            .forEach(n -> n.setStyle("-fx-text-fill: #aaaacc;"));

        stage.setTitle("Snake — Connect");
        stage.setScene(new Scene(box, 280, 310));
        stage.show();
    }

    private void tryConnect(String host, String portStr, String name, Label status) {
        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException ex) {
            status.setText("Invalid port");
            return;
        }
        if (name.isEmpty()) { status.setText("Enter a name"); return; }

        try {
            ServerConnection conn = new ServerConnection(host, port);
            new GameScene(stage, conn, name).show();
        } catch (Exception ex) {
            status.setText("Failed: " + ex.getMessage());
        }
    }
}
