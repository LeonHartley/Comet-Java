package com.cometproject.website.config;

import com.cometproject.website.utilities.FileUtils;
import com.google.gson.Gson;

public class Configuration {
    private static Configuration instance;

    public static Configuration getInstance() {
        if(instance == null) {
            instance = new Gson().fromJson(FileUtils.getContents("./config/CometWebsite.json"), Configuration.class);
        }

        return instance;
    }

    private String dbHost;
    private String dbUsername;
    private String dbPassword;
    private String dbDatabase;
    private int dbPoolSize;

    private String siteUrl;
    private String assetsUrl;
    private int sitePort;

    private String passwordHashMode;
    private String passwordSalt;

    private String apiHost;
    private int apiPort;
    private String apiAuthToken;

    public String getDbHost() {
        return dbHost;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbDatabase() {
        return dbDatabase;
    }

    public int getDbPoolSize() {
        return dbPoolSize;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getAssetsUrl() {
        return assetsUrl;
    }

    public int getSitePort() {
        return sitePort;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public String getPasswordHashMode() {
        return passwordHashMode;
    }

    public String getApiHost() {
        return apiHost;
    }

    public int getApiPort() {
        return apiPort;
    }

    public String getApiAuthToken() {
        return apiAuthToken;
    }
}
