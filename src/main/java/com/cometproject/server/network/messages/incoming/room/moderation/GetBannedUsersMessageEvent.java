package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.settings.RoomBannedListMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class GetBannedUsersMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) return;

        Room room = client.getPlayer().getEntity().getRoom();

        if ((room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        client.send(RoomBannedListMessageComposer.compose(room.getId(), room.getRights().getBannedPlayers()));
    }
}
