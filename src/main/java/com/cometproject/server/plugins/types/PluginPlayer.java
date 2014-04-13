package com.cometproject.server.plugins.types;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;

public class PluginPlayer {
    private Player playerInstance;

    public PluginPlayer(Player playerInstance){
        this.playerInstance = playerInstance;
    }

    public void sendAlert(String title, String message) {
        playerInstance.getSession().send(AdvancedAlertMessageComposer.compose(title, message));
    }

    public int getId() {
        return playerInstance.getData().getId();
    }

    public String getUsername() {
        return playerInstance.getData().getUsername();
    }

    public int getCredits() {
        return playerInstance.getData().getCredits();
    }

    public static PluginPlayer create(Player player) {
        return new PluginPlayer(player);
    }
}
