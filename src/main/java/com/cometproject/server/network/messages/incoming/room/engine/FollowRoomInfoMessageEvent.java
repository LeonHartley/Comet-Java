package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.engine.FollowRoomDataMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class FollowRoomInfoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
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
