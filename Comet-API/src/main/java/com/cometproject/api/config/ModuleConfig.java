package com.cometproject.api.config;

import com.cometproject.api.commands.CommandInfo;

import java.util.Map;

public class ModuleConfig {
    private final String name;
    private final String version;
    private final String entryPoint;

    private Map<String, CommandInfo> commandInfo;

    public ModuleConfig(String name, String version, String entryPoint) {
        this.name = name;
        this.version = version;
        this.entryPoint = entryPoint;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public Map<String, CommandInfo> getCommandInfo() {
        return commandInfo;
    }
}
