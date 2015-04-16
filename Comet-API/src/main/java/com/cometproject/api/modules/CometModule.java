package com.cometproject.api.modules;

import com.cometproject.api.events.EventListenerContainer;
import com.cometproject.api.events.modules.OnModuleLoadEvent;
import com.cometproject.api.events.modules.OnModuleUnloadEvent;
import com.cometproject.api.server.CometGameService;

import java.util.UUID;

public abstract class CometModule implements EventListenerContainer {
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

        this.gameService.getEventHandler().addListenerContainer(this);
    }

    /**
     * Load all the module resources and then fire the "onModuleLoad" event.
     */
    public void loadModule() {
        this.gameService.getEventHandler().handleEvent(new OnModuleLoadEvent());
    }

    /**
     * Unload all module resources and then fire the "onModuleUnload" event.
     */
    public void unloadModule() {
        this.gameService.getEventHandler().handleEvent(new OnModuleUnloadEvent());
    }

    public UUID getModuleId() {
        return moduleId;
    }
}
