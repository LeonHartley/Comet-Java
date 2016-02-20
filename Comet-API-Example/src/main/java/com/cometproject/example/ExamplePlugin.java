package com.cometproject.example;

import com.cometproject.api.config.ModuleConfig;
import com.cometproject.api.events.players.OnPlayerLoginEvent;
import com.cometproject.api.events.players.args.OnPlayerLoginEventArgs;
import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.modules.CometModule;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.api.server.IGameService;

public class ExamplePlugin extends CometModule {
    public ExamplePlugin(ModuleConfig config, IGameService gameService) {
        super(config, gameService);

        this.registerEvent(new OnPlayerLoginEvent(this::onPlayerLogin));

        // register commands
        this.registerChatCommand("!about", this::aboutCommand);
    }

    public void aboutCommand(ISession session, String[] args) {
        session.getPlayer().sendNotif("ExamplePlugin", "This is an example plugin.");
    }

    public void onPlayerLogin(OnPlayerLoginEventArgs eventArgs) {
        IPlayer player = eventArgs.getPlayer();

        player.sendNotif("Welcome!", "Hey " + eventArgs.getPlayer().getData().getUsername() + ", you've received your login bonus!");

        player.getData().increaseCredits(100);
        player.getData().save();

        player.sendBalance();
    }
}
