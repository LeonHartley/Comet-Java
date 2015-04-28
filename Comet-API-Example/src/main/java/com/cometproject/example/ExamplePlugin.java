package com.cometproject.example;

import com.cometproject.api.events.Event;
import com.cometproject.api.events.EventListener;
import com.cometproject.api.events.modules.OnModuleLoadEvent;
import com.cometproject.api.events.modules.OnModuleUnloadEvent;
import com.cometproject.api.modules.CometModule;
import com.cometproject.api.server.CometGameService;

public class ExamplePlugin extends CometModule {
    public ExamplePlugin(CometGameService gameService) {
        super(gameService);
    }

    @EventListener(event = OnModuleLoadEvent.class)
    public void onModuleLoadEvent(OnModuleLoadEvent event) {
        System.out.println("Loaded!");
    }

    @EventListener(event = OnModuleUnloadEvent.class)
    public void onModuleUnloadEvent(OnModuleUnloadEvent event) {
        System.out.println("Unloaded!");
    }
}
