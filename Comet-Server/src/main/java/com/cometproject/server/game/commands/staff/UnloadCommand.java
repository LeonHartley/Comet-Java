package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomReloadListener;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class UnloadCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(!client.getPlayer().getPermissions().getRank().roomFullControl() && client.getPlayer().getEntity().getRoom().getData().getOwnerId() != client.getPlayer().getId())
            return;

        final Room room = client.getPlayer().getEntity().getRoom();

        final RoomReloadListener reloadListener = new RoomReloadListener(room, (players, newRoom) -> {
            for (Player player : players) {
                if (player.getEntity() == null) {
                    player.getSession().send(new AlertMessageComposer(Locale.getOrDefault("command.unload.roomReloaded", "The room was reloaded.")));
                    player.getSession().send(new RoomForwardMessageComposer(newRoom.getId()));
                }
            }
        });

        RoomManager.getInstance().addReloadListener(client.getPlayer().getEntity().getRoom().getId(), reloadListener);
        room.reload();
    }


    @Override
    public String getPermission() {
        return "unload_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.unload.description");
    }
}
