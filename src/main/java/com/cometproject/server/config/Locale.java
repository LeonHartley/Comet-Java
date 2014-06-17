package com.cometproject.server.config;

import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.Map;

public class Locale {
    private static Logger log = Logger.getLogger(Locale.class.getName());
    private static Map<String, String> locale;

    public static void init() {
        Configuration localeFile = new Configuration("./config/locale.properties");
        locale = new FastMap<>();

        for (Map.Entry<Object, Object> prop : localeFile.entrySet()) {
            locale.put((String) prop.getKey(), (String) prop.getValue());
        }
    }

    public static String get(String key) {
        if (locale.containsKey(key))
            return locale.get(key);
        else
            return key;
    }

    public static Map<String, String> getAll() {
        return locale;
    }
}
