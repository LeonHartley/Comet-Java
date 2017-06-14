package com.cometproject.server.network.messages.incoming.room.filter;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.filter.GetRoomFilterListMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class EditWordFilterMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int roomId = msg.readInt();

        final Room room = RoomManager.getInstance().get(roomId);

        if (room == null) {
            return;
        }

        if(!room.getRights().hasRights(client.getPlayer().getId())) {
            // they don't have rights, why should they know what words are in the filter?
            return;
        }

        final boolean add = msg.readBoolean();
        final String word = msg.readString();

        if(!add) {
            room.getFilter().remove(word);
        } else {
            room.getFilter().add(word);
        }
    }
}
