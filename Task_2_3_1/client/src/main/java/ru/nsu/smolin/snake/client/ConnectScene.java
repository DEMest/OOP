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
    private static final String DEFAULT_HOST = "37.195.117.62";
    private static final String DEFAULT_PORT = "6769";
    private static final String DEFAULT_NAME = "Player";
    private static final String CONNECT_BUTTON_TEXT = "Connect";
    private static final String HOST_LABEL_TEXT = "Server host:";
    private static final String PORT_LABEL_TEXT = "Port:";
    private static final String NAME_LABEL_TEXT = "Your name:";
    private static final String WINDOW_TITLE = "Snake — Connect";
    private static final String STATUS_STYLE = "-fx-text-fill: red;";
    private static final String BOX_STYLE = "-fx-background-color: #1a1a2e; -fx-text-fill: white;";
    private static final String LABEL_STYLE = "-fx-text-fill: #aaaacc;";
    private static final double BOX_SPACING = 8;
    private static final double BOX_PADDING = 20;
    private static final double SCENE_WIDTH = 280;
    private static final double SCENE_HEIGHT = 310;

    private final Stage stage;

    public ConnectScene(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        TextField hostField = new TextField(DEFAULT_HOST);
        TextField portField = new TextField(DEFAULT_PORT);
        TextField nameField = new TextField(DEFAULT_NAME);
        Button connectBtn = new Button(CONNECT_BUTTON_TEXT);
        Label statusLabel = new Label();
        statusLabel.setStyle(STATUS_STYLE);

        connectBtn.setDefaultButton(true);
        connectBtn.setOnAction(e -> tryConnect(hostField.getText().trim(),
            portField.getText().trim(), nameField.getText().trim(), statusLabel));

        VBox box = new VBox(BOX_SPACING,
            new Label(HOST_LABEL_TEXT), hostField,
            new Label(PORT_LABEL_TEXT), portField,
            new Label(NAME_LABEL_TEXT), nameField,
            connectBtn, statusLabel
        );
        box.setPadding(new Insets(BOX_PADDING));
        box.setStyle(BOX_STYLE);
        box.getChildren().stream()
            .filter(n -> n instanceof Label)
            .forEach(n -> n.setStyle(LABEL_STYLE));

        stage.setTitle(WINDOW_TITLE);
        stage.setScene(new Scene(box, SCENE_WIDTH, SCENE_HEIGHT));
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
