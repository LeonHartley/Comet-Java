package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.network.sessions.Session;

public class SitCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        PlayerEntity playerEntity = client.getPlayer().getEntity();
        if (playerEntity.hasStatus("sit")) {
            playerEntity.removeStatus("sit");
            playerEntity.markNeedsUpdate();
        } else {
            playerEntity.addStatus("sit", "0.5");
            playerEntity.markNeedsUpdate();
        }


    }

    @Override
    public String getPermission() {
        return "sit_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.sit.description");
    }
}
