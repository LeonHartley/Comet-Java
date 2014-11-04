package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class InitializeRoomMessageEvent implements IEvent {
    // Temporary hacky shizzle for room loading, will keep using for now, I will remove if any issues
    private final LoadHeightmapMessageEvent heightmapMessageEvent = new LoadHeightmapMessageEvent();
    private final AddUserToRoomMessageEvent addUserToRoomMessageEvent = new AddUserToRoomMessageEvent();

    public void handle(Session client, Event msg) {
        int id = msg.readInt();
        String password = msg.readString();

        client.getPlayer().loadRoom(id, password);

        heightmapMessageEvent.handle(client, msg);
        addUserToRoomMessageEvent.handle(client, msg);
    }
}
