package com.cometproject.server.game.rooms.filter;


import com.cometproject.server.storage.queries.filter.FilterDao;
import org.apache.log4j.Logger;

import java.util.Map;

public class WordFilter {
    private Map<String, String> wordfilter;

    public WordFilter() {
        this.loadFilter();
    }

    public void loadFilter() {
        if (this.wordfilter != null) {
            this.wordfilter.clear();
        }

        this.wordfilter = FilterDao.loadWordfilter();

        Logger.getLogger(WordFilter.class.getName()).info("Loaded " + wordfilter.size() + " filtered words");
    }

    public String filter(String message) {
        for (Map.Entry<String, String> word : wordfilter.entrySet()) {
            if (message.contains(word.getKey())) {
                message = message.replace(word.getKey(), word.getValue());
            }
        }

        return message;
    }

    public void save() {
        FilterDao.save(this.wordfilter);
    }
}
