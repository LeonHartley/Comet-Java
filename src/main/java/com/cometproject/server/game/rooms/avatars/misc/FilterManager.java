package com.cometproject.server.game.rooms.avatars.misc;


import com.cometproject.server.storage.queries.filter.FilterDao;
import org.apache.log4j.Logger;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;

public class FilterManager {
    public List<String> blacklist = new ArrayList<>();
    public List<String> whitelist = new ArrayList<>();
    private Logger log = Logger.getLogger(FilterManager.class.getName());

    public FilterManager() {
        init();
    }

    public void init() {
        if (blacklist.size() >= 0) {
            blacklist.clear();
        }

        blacklist = FilterDao.getWordfilterByType("blacklist");

        log.info("Loaded " + blacklist.size() + " blacklisted words");

        if (whitelist.size() >= 0) {
            whitelist.clear();
        }

        whitelist = FilterDao.getWordfilterByType("whitelist");

        log.info("Loaded " + whitelist.size() + " whitelisted words");
    }

    public void save() {
        FilterDao.saveByType("blacklist", blacklist);
        FilterDao.saveByType("whitelist", blacklist);
    }

    private static String removeAccents(String text) {
        return text == null ? null
                : Normalizer.normalize(text, Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public boolean filter(String message) {
        if (message.length() < 3)
            return false;
        for (String AllowedString : whitelist) {
            if (message.contains(AllowedString)) {
                return false;
            }
        }
        message = message.replace("ß", "b");
        message = removeAccents(message);
        message = message.toLowerCase();
        message = message.replaceAll("[^a-zA-Z]+", "");
        message = message.replaceAll("(\\w)\\1+", "$1");

        for (String BlockedString : blacklist) {
            if (message.contains(BlockedString)) {
                return true;
            }
        }

        return false;
    }

    public String getFilteredString(String message) {
        message = message.replace("ß", "b");
        message = removeAccents(message);
        message = message.toLowerCase();
        message = message.replaceAll("[^a-zA-Z]+", "");
        message = message.replaceAll("(\\w)\\1+", "$1");
        return message;
    }

    public void removeblacklistedWord(String word) {
        blacklist.remove(getFilteredString(word));
    }

    public void removewhitelistedWord(String word) {
        whitelist.remove(getFilteredString(word));
    }

    public void addwhitelistedWord(String word) {
        whitelist.add(getFilteredString(word));
    }

    public void addblacklistedWord(String word) {
        blacklist.add(getFilteredString(word));
    }

}
