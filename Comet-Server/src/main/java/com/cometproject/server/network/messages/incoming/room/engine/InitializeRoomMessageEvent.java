package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class InitializeRoomMessageEvent implements Event {
    // Temporary hacky shizzle for room loading, will keep using for now, I will remove if any issues
    public static final LoadHeightmapMessageEvent heightmapMessageEvent = new LoadHeightmapMessageEvent();
    public static final AddUserToRoomMessageEvent addUserToRoomMessageEvent = new AddUserToRoomMessageEvent();

    public void handle(Session client, MessageEvent msg) {
        int id = msg.readInt();
        String password = msg.readString();

        RoomManager.getInstance().initializeRoom(client, id, password);
    }
}
