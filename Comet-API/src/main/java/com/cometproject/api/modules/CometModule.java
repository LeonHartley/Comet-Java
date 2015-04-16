package com.cometproject.api.modules;

import com.cometproject.api.events.listeners.modules.ModuleEventListener;
import com.cometproject.api.server.CometGameService;

import java.util.UUID;

public abstract class CometModule implements ModuleEventListener {
    /**
     * Assign a random UUD to the module at runtime, so the system can tell it apart from other modules.
     */
    private final UUID moduleId;

    /**
     * The bridge between Comet modules & the main server is the GameService, an object which allows you to
     * attach listeners to specific events which are fired within the server and also allows you to access
     * the game server's main components.
     */
    private final CometGameService gameService;

    public CometModule(CometGameService gameService) {
        this.moduleId = UUID.randomUUID();
        this.gameService = gameService;
    }

    /**
     * Load all the module resources and then fire the "onModuleLoad" event.
     */
    public void loadModule() {
        this.onModuleLoad();
    }

    /**
     * Unload all module resources and then fire the "onModuleUnload" event.
     */
    public void unloadModule() {
        this.onModuleUnload();
    }

    public UUID getModuleId() {
        return moduleId;
    }
}
