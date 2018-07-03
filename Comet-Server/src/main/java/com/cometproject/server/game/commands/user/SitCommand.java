package com.cometproject.server.game.commands.user;

import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.sessions.Session;

import static com.cometproject.api.game.rooms.entities.RoomEntityStatus.LAY;
import static com.cometproject.api.game.rooms.entities.RoomEntityStatus.SIT;

public class SitCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        PlayerEntity playerEntity = client.getPlayer().getEntity();

        if(playerEntity.hasStatus(SIT) && playerEntity.hasStatus(LAY)) {
            playerEntity.removeStatus(LAY);
            playerEntity.removeStatus(SIT);
        }

        if(playerEntity.hasStatus(SIT)) {
            playerEntity.removeStatus(SIT);
            playerEntity.markNeedsUpdate();
        } else {
            playerEntity.sit(0.5, playerEntity.getBodyRotation());
        }
    }

    @Override
    public String getPermission() {
        return "sit_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.sit.description");
    }
}
