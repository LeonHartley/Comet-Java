package com.cometproject.server.config;

import com.cometproject.server.boot.Comet;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.Map;

public class Locale {
    private static Logger log = Logger.getLogger(Locale.class.getName());
    private static Map<String, String> locale;

    public static void init() {
        String language = Comet.getServer().getConfig().get("comet.language.locale");
        Configuration localeFile = new Configuration("./config/locale/locale_" + language + ".properties");
        locale = new FastMap<>();

        for (Map.Entry<Object, Object> prop : localeFile.getProperties().entrySet()) {
            locale.put((String) prop.getKey(), (String) prop.getValue());
        }
    }

    public static String get(String key) {
        if (locale.containsKey(key))
            return locale.get(key);
        else
            return key;
    }
}
