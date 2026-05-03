package ru.nsu.smolin.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Конфигурация пиццерии, загружаемая из JSON-файла.
 * Поля маппятся из JSON напрямую через Gson.
 */
public class Config {
    public long simulationTimeMs;
    public long orderRateMs;
    public int warehouseCapacity;

    /**
     * Режим завершения работы:
     * {@code GRACEFUL} — допекаем и доставляем все текущие заказы;
     * иначе — FORCE STOP: прерываем потоки, сериализуем остатки.
     */
    public String shutdownMode;

    public List<BakerConfig>   bakers;
    public List<CourierConfig> couriers;

    public static class BakerConfig {
        public int id;
        public long cookingTimeMs;
    }

    public static class CourierConfig {
        public int id;
        public int trunkSize;
    }

    /**
     * Загружает конфигурацию из {@code config.json} в classpath (src/main/resources).
     */
    public static Config load() throws Exception {
        try (var stream = Config.class.getClassLoader().getResourceAsStream("config.json")) {
            if (stream == null) throw new IllegalStateException("config.json не найден в resources");
            return new Gson().fromJson(new InputStreamReader(stream), Config.class);
        }
    }

    /**
     * Загружает конфигурацию по прямому пути к файлу.
     * Удобно для интеграционных тестов с отдельным конфигом.
     */
    public static Config fromFile(String path) throws Exception {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(path));
        Type type = new TypeToken<Config>() {}.getType();
        return gson.fromJson(reader, type);
    }
}
