package com.cometproject.example;

import com.cometproject.api.events.listeners.modules.ModuleEventListener;
import com.cometproject.api.events.modules.OnModuleLoadEvent;
import com.cometproject.api.events.modules.OnModuleUnloadEvent;
import com.cometproject.api.modules.CometModule;
import com.cometproject.api.server.IGameService;

public class ExamplePlugin extends CometModule implements ModuleEventListener {
    public ExamplePlugin(IGameService gameService) {
        super(gameService);
    }

    @Override
    public void onModuleLoad(OnModuleLoadEvent event) {
        System.out.println("Loaded!");
    }

    @Override
    public void onModuleUnload(OnModuleUnloadEvent event) {
        System.out.println("Unloaded!");
    }
}
