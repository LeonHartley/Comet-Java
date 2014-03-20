package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class InitalizeRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int id = msg.readInt();
        String password = msg.readString();

        client.getPlayer().loadRoom(id, password);
    }
}
