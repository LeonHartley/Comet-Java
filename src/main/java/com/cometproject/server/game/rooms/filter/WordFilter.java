package com.cometproject.server.game.rooms.filter;


import com.cometproject.server.config.CometSettings;
import com.cometproject.server.storage.queries.filter.FilterDao;
import com.cometproject.server.utilities.FilterUtil;
import org.apache.log4j.Logger;

import java.text.Normalizer;
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

    public FilterResult filter(String message) {
        if(CometSettings.wordFilterMode == FilterMode.STRICT) {
            message = FilterUtil.normalize(message.toLowerCase());
        }

        for (Map.Entry<String, String> word : wordfilter.entrySet()) {
            if (message.toLowerCase().contains(word.getKey())) {
                if(CometSettings.wordFilterMode == FilterMode.STRICT)
                    return new FilterResult(true, word.getKey());

                message = message.replace(word.getKey(), word.getValue());
            }
        }

        return new FilterResult(message);
    }

    public void save() {
        FilterDao.save(this.wordfilter);
    }
}
