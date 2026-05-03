package ru.nsu.smolin.snake.client;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ru.nsu.smolin.snake.protocol.*;

import java.util.*;

/**
 * Игровой экран: отправляет JOIN-запрос, принимает обновления состояния
 * от сервера и отрисовывает поле, змеек и таблицу очков через JavaFX Canvas.
 * Камера плавно следит за головой своей змейки.
 */
public class GameScene {
    private static final int VIEWPORT_CELLS = 25;
    private static final double CAMERA_LERP = 0.12;
    private static final Color[] PLAYER_COLORS = {
        Color.rgb(80, 220, 100),
        Color.rgb(80, 180, 255),
        Color.rgb(255, 215, 0),
        Color.rgb(255, 100, 180),
        Color.rgb(255, 150, 50),
        Color.rgb(180, 130, 255),
        Color.rgb(80, 230, 220),
    };

    private final Stage stage;
    private final ServerConnection conn;
    private final String playerName;

    private Canvas canvas;
    private String myId;
    private int gridW, gridH;
    private volatile GameStateDto lastState;
    private final Map<String, Integer> colorMap = new LinkedHashMap<>();
    private int colorCounter = 0;

    private double cameraX = -1, cameraY = -1;

    public GameScene(Stage stage, ServerConnection conn, String playerName) {
        this.stage = stage;
        this.conn = conn;
        this.playerName = playerName;
    }

    public void show() {
        Msg join = new Msg();
        join.setType("JOIN");
        join.setName(playerName);
        conn.send(join);
        conn.startListening(
            this::handleMessage,
            () -> Platform.runLater(() -> stage.setTitle("Snake — Disconnected"))
        );
    }

    private void handleMessage(Msg msg) {
        switch (msg.getType()) {
            case "WELCOME" -> Platform.runLater(() -> {
                myId  = msg.getPlayerId();
                gridW = msg.getGridW();
                gridH = msg.getGridH();
                setupScene();
            });
            case "STATE" -> lastState = msg.getState();
        }
    }

    private void setupScene() {
        canvas = new Canvas();

        BorderPane root = new BorderPane(canvas);
        root.setStyle("-fx-background-color: #111121;");

        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        Scene scene = new Scene(root, 800, 600);
        scene.setOnKeyPressed(e -> {
            Direction dir = keyToDir(e.getCode());
            if (dir != null) {
                Msg msg = new Msg();
                msg.setType("DIR");
                msg.setDir(dir);
                conn.send(msg);
            }
        });

        stage.setTitle("Snake — " + playerName);
        stage.setMinWidth(200);
        stage.setMinHeight(150);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setOnCloseRequest(e -> conn.close());
        stage.show();
        canvas.requestFocus();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        }.start();
    }

    private Direction keyToDir(KeyCode code) {
        return switch (code) {
            case UP,    W -> Direction.UP;
            case DOWN,  S -> Direction.DOWN;
            case LEFT,  A -> Direction.LEFT;
            case RIGHT, D -> Direction.RIGHT;
            default -> null;
        };
    }

    // --- camera & coordinate mapping ----------------------------------------

    private PlayerDto findMyPlayer(GameStateDto state) {
        if (state == null || state.getPlayers() == null || myId == null) return null;
        for (PlayerDto p : state.getPlayers()) {
            if (myId.equals(p.getId())) return p;
        }
        return null;
    }

    private void updateCamera(GameStateDto state) {
        PlayerDto me = findMyPlayer(state);
        if (me == null || me.getSegments() == null || me.getSegments().isEmpty()) return;
        Point head = me.getSegments().get(0);
        double targetX = head.getX() + 0.5;
        double targetY = head.getY() + 0.5;
        if (cameraX < 0) {
            cameraX = targetX;
            cameraY = targetY;
        } else {
            cameraX += (targetX - cameraX) * CAMERA_LERP;
            cameraY += (targetY - cameraY) * CAMERA_LERP;
        }
    }

    /** Cell size: viewport of VIEWPORT_CELLS x VIEWPORT_CELLS fits within the canvas. */
    private double cell() {
        return Math.min(canvas.getWidth(), canvas.getHeight()) / VIEWPORT_CELLS;
    }

    /** Map grid coordinate to screen pixel. */
    private double px(double gridCol) {
        double c = cell();
        return canvas.getWidth() / 2.0 + (gridCol - cameraX) * c;
    }

    private double py(double gridRow) {
        double c = cell();
        return canvas.getHeight() / 2.0 + (gridRow - cameraY) * c;
    }

    // --- rendering -----------------------------------------------------------

    private void render() {
        GameStateDto state = lastState;
        if (canvas == null || state == null || gridW == 0 || gridH == 0) return;
        updateCamera(state);
        if (cameraX < 0) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth(), h = canvas.getHeight();
        double c = cell();

        // Full background
        gc.setFill(Color.rgb(13, 13, 26));
        gc.fillRect(0, 0, w, h);

        // Visible cell range
        double halfView = VIEWPORT_CELLS / 2.0 + 1;
        int minCol = Math.max(0, (int) Math.floor(cameraX - halfView));
        int maxCol = Math.min(gridW, (int) Math.ceil(cameraX + halfView));
        int minRow = Math.max(0, (int) Math.floor(cameraY - halfView));
        int maxRow = Math.min(gridH, (int) Math.ceil(cameraY + halfView));

        // Grid area background
        double gx0 = px(0), gy0 = py(0);
        double gx1 = px(gridW), gy1 = py(gridH);
        gc.setFill(Color.rgb(17, 17, 33));
        gc.fillRect(Math.max(0, gx0), Math.max(0, gy0),
            Math.min(w, gx1) - Math.max(0, gx0),
            Math.min(h, gy1) - Math.max(0, gy0));

        // Grid lines (only visible)
        gc.setStroke(Color.rgb(30, 30, 55));
        gc.setLineWidth(0.5);
        for (int x = minCol; x <= maxCol; x++) {
            double sx = px(x);
            gc.strokeLine(sx, Math.max(0, gy0), sx, Math.min(h, gy1));
        }
        for (int y = minRow; y <= maxRow; y++) {
            double sy = py(y);
            gc.strokeLine(Math.max(0, gx0), sy, Math.min(w, gx1), sy);
        }

        // Border of the grid (red line)
        gc.setStroke(Color.rgb(200, 50, 50, 0.6));
        gc.setLineWidth(2);
        gc.strokeRect(px(0), py(0), gridW * c, gridH * c);

        // Food
        if (state.getFood() != null) {
            for (FoodDto f : state.getFood()) {
                if (f.getX() < minCol - 1 || f.getX() > maxCol || f.getY() < minRow - 1 || f.getY() > maxRow) continue;
                double fx = px(f.getX()), fy = py(f.getY());

                boolean temporal = f.getExpiresAtTick() > 0;
                double alpha = 1.0;
                if (temporal && state.getTick() > 0) {
                    long ticksLeft = f.getExpiresAtTick() - state.getTick();
                    // Мигание в последние ~3 сек (20 тиков)
                    if (ticksLeft < 20) {
                        alpha = (ticksLeft % 4 < 2) ? 0.3 : 1.0;
                    }
                }

                if (temporal) {
                    gc.setFill(Color.rgb(255, 180, 50, 0.25 * alpha));
                    gc.fillOval(fx + c * 0.1, fy + c * 0.1, c * 0.8, c * 0.8);
                    gc.setFill(Color.rgb(255, 200, 60, alpha));
                    gc.fillOval(fx + c * 0.25, fy + c * 0.25, c * 0.5, c * 0.5);
                } else {
                    gc.setFill(Color.rgb(255, 60, 60, 0.25));
                    gc.fillOval(fx + c * 0.1, fy + c * 0.1, c * 0.8, c * 0.8);
                    gc.setFill(Color.rgb(255, 80, 80));
                    gc.fillOval(fx + c * 0.25, fy + c * 0.25, c * 0.5, c * 0.5);
                }
            }
        }

        // Snakes
        if (state.getPlayers() != null) {
            for (PlayerDto p : state.getPlayers()) {
                if (p.getSegments() == null || p.getSegments().isEmpty()) continue;
                Color base = getColor(p.getId());
                if (!p.isAlive()) base = base.deriveColor(0, 0.3, 0.35, 0.5);

                for (int i = p.getSegments().size() - 1; i >= 0; i--) {
                    Point seg = p.getSegments().get(i);
                    if (seg.getX() < minCol - 1 || seg.getX() > maxCol || seg.getY() < minRow - 1 || seg.getY() > maxRow) continue;
                    boolean isHead = (i == 0);
                    double margin = isHead ? c * 0.05 : c * 0.1;
                    double brightness = isHead ? 1.0 : Math.max(0.55, 1.0 - (double) i / p.getSegments().size() * 0.4);
                    gc.setFill(isHead ? base.brighter() : base.deriveColor(0, 1, brightness, 1));
                    double r = Math.max(2, c * 0.25);
                    gc.fillRoundRect(px(seg.getX()) + margin, py(seg.getY()) + margin,
                        c - 2 * margin, c - 2 * margin, r, r);
                }

                // Eyes
                Point head = p.getSegments().get(0);
                if (p.isAlive() && head.getX() >= minCol - 1 && head.getX() <= maxCol && head.getY() >= minRow - 1 && head.getY() <= maxRow) {
                    drawEyes(gc, head, p.getDirection(), c);
                }

                // Name label
                if (head.getX() >= minCol - 2 && head.getX() <= maxCol + 2 && head.getY() >= minRow - 2 && head.getY() <= maxRow + 2) {
                    double labelY = py(head.getY()) - c * 0.15;
                    double fontSize = Math.max(8, c * 0.45);
                    gc.setFont(Font.font("Monospace", FontWeight.BOLD, fontSize));
                    String label = (p.getId().equals(myId) ? "▶ " : "") + p.getName() + "  " + p.getScore();
                    gc.setFill(Color.rgb(0, 0, 0, 0.65));
                    gc.fillText(label, px(head.getX()) + 1, labelY + 1);
                    gc.setFill(p.getId().equals(myId) ? Color.WHITE : Color.rgb(200, 200, 210));
                    gc.fillText(label, px(head.getX()), labelY);
                }
            }
        }

        // Scoreboard
        drawScoreboard(gc, c);
    }

    private void drawEyes(GraphicsContext gc, Point head, Direction dir, double c) {
        double cx = px(head.getX()) + c / 2;
        double cy = py(head.getY()) + c / 2;
        double r = Math.max(1.5, c * 0.1);
        double d = c * 0.2;
        double[][] offsets = switch (dir != null ? dir : Direction.RIGHT) {
            case RIGHT -> new double[][]{{ d, -d}, { d,  d}};
            case LEFT  -> new double[][]{{-d, -d}, {-d,  d}};
            case UP    -> new double[][]{{-d, -d}, { d, -d}};
            case DOWN  -> new double[][]{{-d,  d}, { d,  d}};
        };
        gc.setFill(Color.WHITE);
        for (double[] o : offsets) gc.fillOval(cx + o[0] - r, cy + o[1] - r, r * 2, r * 2);
    }

    private void drawScoreboard(GraphicsContext gc, double c) {
        GameStateDto state = lastState;
        if (state == null || state.getPlayers() == null || state.getPlayers().isEmpty()) return;
        List<PlayerDto> sorted = new ArrayList<>(state.getPlayers());
        sorted.sort((a, b) -> b.getScore() - a.getScore());

        double fontSize = Math.max(10, 14);
        double rowH = fontSize + 4;
        double sbW = fontSize * 12;
        double sbH = sorted.size() * rowH + 10;
        double sbX = canvas.getWidth() - sbW - 8;
        double sbY = 8;

        gc.setFill(Color.rgb(0, 0, 0, 0.65));
        gc.fillRoundRect(sbX, sbY, sbW, sbH, 6, 6);
        gc.setFont(Font.font("Monospace", FontWeight.BOLD, fontSize));

        for (int i = 0; i < sorted.size(); i++) {
            PlayerDto p = sorted.get(i);
            Color col = getColor(p.getId());
            if (!p.isAlive()) col = col.deriveColor(0, 0.4, 0.5, 0.7);
            gc.setFill(col);
            int maxName = 10;
            String line = (i + 1) + ". " + truncate(p.getName(), maxName) + " " + p.getScore();
            if (p.getId().equals(myId)) line = ">" + line.substring(1);
            gc.fillText(line, sbX + 5, sbY + fontSize + i * rowH);
        }
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private Color getColor(String id) {
        return PLAYER_COLORS[colorMap.computeIfAbsent(id, k -> colorCounter++ % PLAYER_COLORS.length)];
    }
}
