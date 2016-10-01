package com.cometproject.server.game.commands.gimmicks;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class PunchCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.punch.none", "Who do you want to punch?"), client);
            return;
        }

        String punchedPlayer = params[0];
        
        RoomEntity entity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByName(punchedPlayer, RoomEntityType.PLAYER);

        if (entity == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), "* " + client.getPlayer().getData().getUsername() + " " + Locale.getOrDefault("command.punch.word", "punched") + " " + entity.getUsername() + " *", 34));
    }

    @Override
    public String getPermission() {
        return "punch_command";
    }
    
    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.punch.description");
    }
}
