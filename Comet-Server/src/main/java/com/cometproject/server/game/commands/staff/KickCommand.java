package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.sessions.Session;


public class KickCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            sendNotif(Locale.getOrDefault("command.kick.none", "Who do you want to kick?"), client);
            return;
        }
        
        String username = params[0];

        PlayerEntity entity = (PlayerEntity) client.getPlayer().getEntity().getRoom().getEntities().getEntityByName(username, RoomEntityType.PLAYER);

        if (entity == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (entity.getUsername().equals(client.getPlayer().getData().getUsername())) {
            return;
        }

        if (!entity.getPlayer().getPermissions().getRank().roomKickable()) {
            sendNotif(Locale.getOrDefault("command.kick.unkickable", "You can't kick this player!"), client);
            return;
        }

        entity.kick();
        isExecuted(client);
    }

    @Override
    public String getPermission() {
        return "kick_command";
    }
    
    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.kick.description");
    }
}
