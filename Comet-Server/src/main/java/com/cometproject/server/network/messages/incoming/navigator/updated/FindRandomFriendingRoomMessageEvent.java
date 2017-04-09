package com.cometproject.server.network.messages.incoming.navigator.updated;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class FindRandomFriendingRoomMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final String data = msg.readString();

        if (data.equals("random_friending_room")) {
            final int roomId = RoomManager.getInstance().getRandomActiveRoom();

            if (roomId > 0)
                client.send(new RoomForwardMessageComposer(roomId));
        }
    }
}
