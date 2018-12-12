package com.cometproject.server.game.rooms.filter;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.rooms.filter.FilterMode;
import com.cometproject.server.storage.queries.filter.FilterDao;
import com.cometproject.server.utilities.FilterUtil;
import org.apache.logging.log4j.LogManager;

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

        LogManager.getLogger(WordFilter.class.getName()).info("Loaded {} filtered words", wordfilter.size());
    }

    public FilterResult filter(String message) {
        String filteredMessage = message;

        if (CometSettings.wordFilterMode == FilterMode.STRICT) {
            message = FilterUtil.process(message.toLowerCase());
        }

        for (Map.Entry<String, String> word : wordfilter.entrySet()) {
            if (message.toLowerCase().contains(word.getKey())) {
                if (CometSettings.wordFilterMode == FilterMode.STRICT)
                    return new FilterResult(true, word.getKey());

                filteredMessage = filteredMessage.replace("(?i)" + word.getKey(), word.getValue());
            }
        }

        return new FilterResult(filteredMessage, !message.equals(filteredMessage));
    }

    public void save() {
        FilterDao.save(this.wordfilter);
    }
}
