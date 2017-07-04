package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomActionMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class RoomOptionCommand extends ChatCommand {
    public void execute(Session client, String[] params) {
        final PlayerEntity playerEntity = client.getPlayer().getEntity();

        if ((playerEntity != null) && (playerEntity.getRoom() != null)) {
            if ((playerEntity.getRoom().getData().getOwnerId() != client.getPlayer().getId()) && (!client.getPlayer().getPermissions().getRank().modTool())) {
                return;
            }
            if (params.length != 1) {
                sendWhisper(Locale.getOrDefault("command.roomoption.none", "To change the room option type :roomoption %option% (You're able to use: shake & disco)"), client);
                return;
            }

            if (params[0].toLowerCase().equals(Locale.getOrDefault("command.roomoption.shake", "shake"))) {
                playerEntity.getRoom().getEntities().broadcastMessage(new RoomActionMessageComposer(1));
                return;
            }

            if (params[0].toLowerCase().equals(Locale.getOrDefault("command.roomoption.disco", "disco"))) {
                playerEntity.getRoom().getEntities().broadcastMessage(new RoomActionMessageComposer(3));
                return;
            }

            if ((params[0].toLowerCase().equals(Locale.getOrDefault("command.roomoption.rotate", "rotate"))) && (client.getPlayer().getPermissions().getRank().roomFullControl())) {
                playerEntity.getRoom().getEntities().broadcastMessage(new RoomActionMessageComposer(0));
                return;
            }

            sendWhisper(Locale.getOrDefault("command.roomoption.none", "To change the room option type :roomoption %option% (You're able to use: shake & disco)"), client);
        }
    }

    public String getPermission() {
        return "roomoption_command";
    }

    public String getParameter() {
        return "%option%";
    }

    public String getDescription() {
        return Locale.get("command.roomoption.description");
    }
}
