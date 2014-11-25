package com.cometproject.server.game.commands.gimmicks;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class SexCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        if(params.length != 1) return;

        String sexedPlayer = params[0];

        GenericEntity entity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByName(sexedPlayer, RoomEntityType.PLAYER);

        if(entity == null) return;

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(WisperMessageComposer.compose(client.getPlayer().getEntity().getId(), "* " + client.getPlayer().getData().getUsername() + " sexed " + entity.getUsername() + " *", 34));
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(ActionMessageComposer.compose(entity.getId(), 7));
    }

    @Override
    public String getPermission() {
        return "sex_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.sex.description");
    }
}
