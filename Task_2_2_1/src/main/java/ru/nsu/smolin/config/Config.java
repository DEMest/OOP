package ru.nsu.smolin.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Config {
    public long simulationTimeMs;
    public long orderRateMs;
    public int warehouseCapacity;
    public String shutdownMode;
    public List<BakerConfig> bakers;
    public List<CourierConfig> couriers;

    public static class BakerConfig {
        public int id;
        public long cookingTimeMs;
    }

    public static class CourierConfig {
        public int id;
        public int trunkSize;
    }

    public static Config fromFile(String filename) throws Exception {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(filename));
        Type type = new TypeToken<Config>() {}.getType();
        return gson.fromJson(reader, type);
    }
}