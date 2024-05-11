package ir.doodmood.mashtframework.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Config {
    private static Config config = null;
    private JsonObject configJson = null;
    private HashMap<String, HashMap<String, Object>> cache = new HashMap<>();
            // (key, class) -> obj
    private Config() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("properties.json");

        if (is == null) configJson = new JsonObject();
        else configJson = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
    }

    public static Config getInstance() {
        if (config == null)
            config = new Config();
        return config;
    }

    public Object get(String key, Class gClass) {
        if (cache.containsKey(key) && cache.get(key).containsKey(gClass.getName()))
            return cache.get(key).get(gClass.getName());

        Gson gson = new Gson();
        Object ret = gson.fromJson(configJson.get(key), gClass);

        cache.putIfAbsent(key, new HashMap<>());
        cache.get(key).put(gClass.getName(), ret);

        return ret;
    }
}
