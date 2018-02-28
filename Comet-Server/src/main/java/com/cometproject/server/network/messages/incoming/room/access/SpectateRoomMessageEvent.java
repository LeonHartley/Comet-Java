package com.cometproject.server.network.messages.incoming.room.access;

import com.cometproject.server.game.rooms.RoomQueue;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class SpectateRoomMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        int roomId = client.getPlayer().getEntity().getRoom().getId();

        if (!RoomQueue.getInstance().hasQueue(roomId)) {
            client.send(new HotelViewMessageComposer());
            return;
        }

        // enter the room normally but then make sure they are invisible & are spectating.
        client.getPlayer().setSpectatorRoomId(roomId);

        RoomQueue.getInstance().removePlayerFromQueue(roomId, client.getPlayer().getId());
        //client.send(new RoomForwardMessageComposer(roomId));

        // re-do room checks like max player check etc
        client.getPlayer().getEntity().joinRoom(client.getPlayer().getEntity().getRoom(), "");
    }
}
