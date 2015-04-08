package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.navigator.CreateRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class CreateRoomMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String name = msg.readString();
        String description = msg.readString();
        String model = msg.readString();
        int category = msg.readInt();
        int maxVisitors = msg.readInt();
        int tradeState = msg.readInt();

        if (RoomManager.getInstance().getModel(model) == null) {
            client.send(new MotdNotificationComposer("Invalid room model"));
            return;
        }

        int roomId = RoomManager.getInstance().createRoom(name, description, model, category, maxVisitors, tradeState, client);
        client.send(new CreateRoomMessageComposer(roomId, name));
    }
}
