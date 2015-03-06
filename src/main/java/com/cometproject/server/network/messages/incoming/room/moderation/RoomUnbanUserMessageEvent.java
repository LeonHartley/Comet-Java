package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.settings.RoomPlayerUnbannedMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class RoomUnbanUserMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int playerId = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || (room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        if (room.getRights().hasBan(playerId)) {
            room.getRights().removeBan(playerId);
        }

        client.send(new RoomPlayerUnbannedMessageComposer(room.getId(), playerId));
    }
}
