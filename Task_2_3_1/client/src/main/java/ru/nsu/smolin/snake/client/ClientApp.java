package ru.nsu.smolin.snake.client;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Точка входа JavaFX-клиента. Открывает экран подключения {@link ConnectScene}.
 */
public class ClientApp extends Application {
    @Override
    public void start(Stage stage) {
        new ConnectScene(stage).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
