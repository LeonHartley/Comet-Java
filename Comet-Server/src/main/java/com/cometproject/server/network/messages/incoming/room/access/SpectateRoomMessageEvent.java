package com.cometproject.server.network.messages.incoming.room.access;

import com.cometproject.server.game.rooms.RoomQueue;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class SpectateRoomMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int roomId = msg.readInt();

        if(!RoomQueue.getInstance().hasQueue(roomId)) {
            return;
        }

        // enter the room normally but then make sure they are invisible & are spectating.
        client.getPlayer().setSpectatorRoomId(roomId);

        RoomQueue.getInstance().removePlayerFromQueue(roomId, client.getPlayer().getId());
        client.send(new RoomForwardMessageComposer(roomId));
    }
}
