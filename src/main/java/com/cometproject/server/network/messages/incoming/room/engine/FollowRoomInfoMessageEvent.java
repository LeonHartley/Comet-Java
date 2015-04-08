package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.FollowRoomDataMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class FollowRoomInfoMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int roomId = msg.readInt();
        boolean isInSameRoom = msg.readInt() == 1;

        if (roomId != 0 && !isInSameRoom) {
            RoomData roomData = RoomManager.getInstance().getRoomData(roomId);

            if (roomData == null) {
                return;
            }

            client.send(new FollowRoomDataMessageComposer(roomData));
        }
    }
}
