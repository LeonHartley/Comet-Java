package com.cometproject.website.gamedata;

import com.cometproject.website.storage.dao.gamedata.GameDataDao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameDataCache {
    private final Map<String, String> externalVariables = new ConcurrentHashMap<>();
    private final Map<String, String> externalTexts = new ConcurrentHashMap<>();

    private String cachedVariables;
    private String cachedTexts;

    public GameDataCache() {
        this.reload();
    }

    public void reload() {
        this.externalVariables.clear();
        GameDataDao.getAllVariables(this.externalVariables);

        this.externalTexts.clear();
        GameDataDao.getAllTexts(this.externalTexts);

        this.buildVarString();
        this.buildTextsString();
    }

    private void buildVarString() {
        final StringBuilder stringBuilder = new StringBuilder();

        for(Map.Entry<String, String> var : this.externalVariables.entrySet()) {
            stringBuilder.append(var.getKey() + "=" + var.getValue() + "\n");
        }

        this.cachedVariables = stringBuilder.toString();
    }

    private void buildTextsString() {
        final StringBuilder stringBuilder = new StringBuilder();

        for(Map.Entry<String, String> var : this.externalTexts.entrySet()) {
            stringBuilder.append(var.getKey() + "=" + var.getValue() + "\n");
        }

        this.cachedTexts = stringBuilder.toString();
    }

    public String getCachedVariables() {
        return cachedVariables;
    }

    public String getCachedTexts() {
        return cachedTexts;
    }

    private static GameDataCache gameDataCache;

    public static GameDataCache getInstance() {
        if(gameDataCache == null) {
            gameDataCache = new GameDataCache();
        }

        return gameDataCache;
    }
}
