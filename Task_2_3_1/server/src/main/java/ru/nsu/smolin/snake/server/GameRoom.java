package ru.nsu.smolin.snake.server;

import ru.nsu.smolin.snake.protocol.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Игровая комната — хранит полное состояние одной партии:
 * змейки всех игроков, еду на поле и ссылки на обработчики клиентов.
 * Запускает собственный игровой цикл в фоновом потоке-демоне,
 * который тикает каждые {@value #TICK_MS} мс.
 * Методы {@link #addPlayer} и {@link #removePlayer} вызываются
 * из потоков клиентов, поэтому коллекции игроков потокобезопасны.
 */
public class GameRoom {
    private static final int MAX_FOOD = 200;
    private static final int FOOD_EXPIRE_TICKS = 133; // ~20 секунд при 150ms тике
    private static final int TICK_MS = 150;
    private static final int RESPAWN_TICKS = 20;

    private final int W, H;
    private final Map<String, Snake> snakes = new ConcurrentHashMap<>();  // playerId → змейка; ConcurrentHashMap т.к. addPlayer/removePlayer вызываются из потоков клиентов
    private final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();  // playerId → обработчик клиента; нужен для рассылки состояния
    private final List<FoodDto> food = new ArrayList<>();  // еда на поле; изменяется только внутри gameTick (один поток)
    private final AtomicInteger idGen = new AtomicInteger(0);  // счётчик для уникальных ID игроков; AtomicInteger т.к. incrementAndGet вызывается из разных потоков
    private final Random rng = new Random();
    private long tick = 0;

    public GameRoom(int w, int h) {
        this.W = w;
        this.H = h;
    }

    /**
     * Запускает игровой цикл: создаёт фоновый поток-демон и планирует
     * вызов {@link #gameTick()} каждые {@value #TICK_MS} мс.
     * Поток помечен daemon — JVM не будет ждать его завершения при выходе.
     */
    public void startGameLoop() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "game-loop");
            t.setDaemon(true);
            return t;
        });
        exec.scheduleAtFixedRate(this::gameTick, TICK_MS, TICK_MS, TimeUnit.MILLISECONDS);
    }

    public String addPlayer(String name, ClientHandler handler) {
        String id = "p" + idGen.incrementAndGet();
        Point pos = findSafeSpawn();
        Snake snake = new Snake(id, name, pos.getX(), pos.getY());
        snakes.put(id, snake);
        clients.put(id, handler);
        return id;
    }

    public void removePlayer(String id) {
        snakes.remove(id);
        clients.remove(id);
    }

    public void setDirection(String playerId, Direction dir) {
        Snake s = snakes.get(playerId);
        if (s != null && s.alive && dir != s.direction.opposite()) {
            s.pendingDir = dir;
        }
    }

    /**
     * Один шаг игровой логики: отсчитывает таймеры респауна,
     * двигает живые змейки, проверяет столкновения со стенами и телами,
     * убивает столкнувшихся, пополняет еду до {@value #MAX_FOOD}
     * и рассылает новое состояние всем клиентам.
     * Всегда выполняется строго в одном потоке — {@code game-loop}.
     */
    private void gameTick() {
        tick++;

        // Удаляем протухшую еду
        food.removeIf(f -> f.getExpiresAtTick() > 0 && tick > f.getExpiresAtTick());

        for (Snake s : snakes.values()) {
            if (!s.alive) {
                s.respawnTicks--;
                if (s.respawnTicks <= 0) {
                    respawnSnake(s);
                }
            }
        }

        Map<String, Point> newHeads = new HashMap<>();
        for (Snake s : snakes.values()) {
            if (!s.alive) continue;
            s.direction = s.pendingDir;
            Point head = s.head();
            newHeads.put(s.playerId, new Point(head.getX() + s.direction.getDx(), head.getY() + s.direction.getDy()));
        }

        Set<String> died = new HashSet<>();
        for (Map.Entry<String, Point> e : newHeads.entrySet()) {
            Point p = e.getValue();
            if (p.getX() < 0 || p.getX() >= W || p.getY() < 0 || p.getY() >= H) {
                died.add(e.getKey());
            }
        }

        for (Snake s : snakes.values()) {
            if (!s.alive || died.contains(s.playerId)) continue;
            Point newHead = newHeads.get(s.playerId);
            boolean ate = food.removeIf(f -> f.getX() == newHead.getX() && f.getY() == newHead.getY());
            if (ate) s.score++;
            s.segments.addFirst(newHead);
            if (!ate) s.segments.pollLast();
        }

        for (Snake s : snakes.values()) {
            if (!s.alive || died.contains(s.playerId)) continue;
            Point head = s.head();
            outer:
            for (Snake other : snakes.values()) {
                if (!other.alive) continue;
                List<Point> segs = new ArrayList<>(other.segments);
                int startIdx = (other == s) ? 1 : 0;
                for (int i = startIdx; i < segs.size(); i++) {
                    if (segs.get(i).getX() == head.getX() && segs.get(i).getY() == head.getY()) {
                        died.add(s.playerId);
                        break outer;
                    }
                }
            }
        }

        // При смерти змейки — на каждый сегмент кладём временную еду
        for (String id : died) {
            Snake s = snakes.get(id);
            if (s != null) {
                long expiry = tick + FOOD_EXPIRE_TICKS;
                for (Point seg : s.segments) {
                    food.add(new FoodDto(seg.getX(), seg.getY(), expiry));
                }
                s.alive = false;
                s.respawnTicks = RESPAWN_TICKS;
            }
        }

        while (food.size() < MAX_FOOD) {
            spawnFood();
        }

        broadcastState();
    }

    private void respawnSnake(Snake s) {
        Point pos = findSafeSpawn();
        s.segments.clear();
        s.segments.addFirst(new Point(pos.getX(), pos.getY()));
        s.segments.addLast(new Point(pos.getX() - 1, pos.getY()));
        s.segments.addLast(new Point(pos.getX() - 2, pos.getY()));
        s.direction = Direction.RIGHT;
        s.pendingDir = Direction.RIGHT;
        s.score = 0;
        s.alive = true;
    }

    private Point findSafeSpawn() {
        Set<String> occupied = new HashSet<>();
        for (Snake s : snakes.values()) {
            for (Point seg : s.segments) occupied.add(seg.getX() + "," + seg.getY());
        }
        int x, y, attempts = 0;
        do {
            x = 3 + rng.nextInt(Math.max(1, W - 6));
            y = rng.nextInt(H);
            attempts++;
        } while (attempts < 100 && (
            occupied.contains(x + "," + y) ||
            occupied.contains((x - 1) + "," + y) ||
            occupied.contains((x - 2) + "," + y)
        ));
        return new Point(x, y);
    }

    private void spawnFood() {
        Set<String> occupied = new HashSet<>();
        for (Snake s : snakes.values()) {
            for (Point seg : s.segments) occupied.add(seg.getX() + "," + seg.getY());
        }
        for (FoodDto f : food) occupied.add(f.getX() + "," + f.getY());
        int x, y, attempts = 0;
        do {
            x = rng.nextInt(W);
            y = rng.nextInt(H);
            attempts++;
        } while (attempts < 100 && occupied.contains(x + "," + y));
        food.add(new FoodDto(x, y, -1));
    }

    private void broadcastState() {
        GameStateDto state = buildState();
        for (ClientHandler handler : clients.values()) {
            handler.send(state);
        }
    }

    private GameStateDto buildState() {
        List<PlayerDto> players = snakes.values().stream()
            .map(s -> new PlayerDto(s.playerId, s.playerName, s.score, s.alive,
                                    new ArrayList<>(s.segments), s.direction))
            .collect(Collectors.toList());
        return new GameStateDto(players, new ArrayList<>(food), tick);
    }

    public int getW() { return W; }
    public int getH() { return H; }
}
