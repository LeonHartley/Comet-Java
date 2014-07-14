package com.cometproject.server.config;

import com.cometproject.server.storage.queries.locale.LocaleDao;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.Map;

public class Locale {
    private static Logger log = Logger.getLogger(Locale.class.getName());
    private static Map<String, String> locale;

    public static void init() {
        reload();
    }

    public static void reload() {
        if(locale != null)
            locale.clear();

        locale = LocaleDao.getAll();
        log.info("Loaded " + locale.size() + " locale strings");
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
