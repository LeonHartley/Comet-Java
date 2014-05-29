package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import org.apache.log4j.Logger;

public class InitalizeRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int id = msg.readInt();
        String password = msg.readString();

        if(password == null) {
            Logger.getLogger("InitializeRoom").error("Password is null!");
            return;
        } else if(client == null) {
            Logger.getLogger("InitializeRoom").error("Client is null!");
            return;
        } else if(client.getPlayer() == null) {
            Logger.getLogger("InitializeRoom").error("Player is null!");
            return;
        }

        client.getPlayer().loadRoom(id, password);
    }
}
