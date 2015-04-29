package com.cometproject.example;

import com.cometproject.api.events.EventListener;
import com.cometproject.api.events.modules.OnModuleLoadEvent;
import com.cometproject.api.events.modules.OnModuleUnloadEvent;
import com.cometproject.api.events.players.OnPlayerLoginEvent;
import com.cometproject.api.modules.CometModule;
import com.cometproject.api.server.IGameService;

public class ExamplePlugin extends CometModule {
    public ExamplePlugin(IGameService gameService) {
        super(gameService);
    }

    @EventListener(event = OnModuleLoadEvent.class)
    public void onModuleLoad(OnModuleLoadEvent event) {

    }

    @EventListener(event = OnModuleUnloadEvent.class)
    public void onModuleUnload(OnModuleUnloadEvent event) {

    }

    @EventListener(event = OnPlayerLoginEvent.class)
    public void onPlayerLogin(OnPlayerLoginEvent event) {
        event.getPlayer().getData().increaseCredits(100);
        event.getPlayer().getData().save();

        event.getPlayer().sendBalance();
        event.getPlayer().sendNotif("Welcome!", "Hey, " + event.getPlayer().getData().getUsername() + ", you've received your login bonus!");
    }
}
