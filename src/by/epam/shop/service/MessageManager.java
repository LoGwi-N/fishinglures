package by.epam.shop.service;

import com.google.gson.Gson;

import java.util.*;

public class MessageManager {
    public final static ResourceBundle bundleDefault = ResourceBundle.getBundle("resources");
    public final static ResourceBundle bundleRU = ResourceBundle.getBundle("resources", new Locale("ru", "RU"));
    public final static ResourceBundle bundleEN = ResourceBundle.getBundle("resources", new Locale("en", "EN"));

    public static Map<String, String> convertResourceBundleToMap(ResourceBundle resource) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> keys = resource.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            map.put(key, resource.getString(key));
        }
        return map;
    }

    public static String createJSON(Map<String, String> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

}
