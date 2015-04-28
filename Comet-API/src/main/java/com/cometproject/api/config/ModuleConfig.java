package com.cometproject.api.config;

public class ModuleConfig {
    private String name;
    private String version;
    private String entryPoint;

    public ModuleConfig(String name, String version, String entryPoint) {
        this.name = name;
        this.version = version;
        this.entryPoint = entryPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }
}
