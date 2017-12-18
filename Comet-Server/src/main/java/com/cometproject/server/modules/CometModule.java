package com.cometproject.server.modules;

import com.google.gson.JsonObject;

public class CometModule {

    private final String path;
    private final String alias;
    private final JsonObject config;
    private ClassLoader classLoader;

    public CometModule(String path, String alias, JsonObject config) {
        this.path = path;
        this.alias = alias;
        this.config = config;
    }

    public String getPath() {
        return path;
    }

    public String getAlias() {
        return alias;
    }

    public JsonObject getConfig() {
        return config;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
