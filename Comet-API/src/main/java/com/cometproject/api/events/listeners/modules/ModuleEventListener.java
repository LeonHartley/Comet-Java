package com.cometproject.api.events.listeners.modules;

public interface ModuleEventListener {
    void onModuleLoad();

    void onModuleUnload();
}
