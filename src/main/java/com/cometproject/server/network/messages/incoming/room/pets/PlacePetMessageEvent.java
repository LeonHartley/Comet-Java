package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PlacePetMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        throw new Exception("Feature currently not supported.");
    }
}
