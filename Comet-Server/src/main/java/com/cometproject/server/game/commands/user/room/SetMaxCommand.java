package com.cometproject.server.game.commands.user.room;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.GameContext;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class SetMaxCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.get("command.setmax.invalid"), client);
            return;
        }

        final Room room = client.getPlayer().getEntity().getRoom();
        final boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());
        final boolean isStaff = client.getPlayer().getPermissions().getRank().roomFullControl();

        if (hasRights || isStaff) {
            try {
                final int maxPlayers = Integer.parseInt(params[0]);

                if ((maxPlayers > CometSettings.roomMaxPlayers && !isStaff) || maxPlayers < 1) {
                    sendNotif(Locale.get("command.setmax.toomany").replace("%i", CometSettings.roomMaxPlayers + ""), client);
                    return;
                }

                room.getData().setMaxUsers(maxPlayers);
                GameContext.getCurrent().getRoomService().saveRoomData(room.getData());

                sendNotif(Locale.get("command.setmax.done").replace("%i", maxPlayers + ""), client);
                room.getEntities().broadcastMessage(new RoomDataMessageComposer(room));
            } catch (Exception e) {
                sendNotif(Locale.get("command.setmax.invalid"), client);
            }
        }
    }

    @Override
    public String getPermission() {
        return "setmax_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.amount", "%amount%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.setmax.description");
    }
}
