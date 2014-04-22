package com.cometproject.server.game.commands.user;


import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.network.sessions.Session;

public class LayCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        PlayerEntity playerEntity = client.getPlayer().getEntity();
        if (playerEntity.hasStatus("lay")) {
            playerEntity.removeStatus("lay");
            playerEntity.markNeedsUpdate();
        } else {
            playerEntity.addStatus("lay", "0.5");
            playerEntity.markNeedsUpdate();
        }
    }

    @Override
    public String getPermission() {
        return "lay_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.lay.description");
    }
}
