package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.navigator.CreateRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class CreateRoomMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String name = msg.readString();
        String description = msg.readString();
        String model = msg.readString();
        int category = msg.readInt();
        int maxVisitors = msg.readInt();
        int tradeState = msg.readInt();

        if(client.getPlayer().getRooms().size() >= 100) {
            return;
        }

        if(((int) Comet.getTime()) - client.getPlayer().getLastRoomCreated() < 60) {
            return;
        }

        if (RoomManager.getInstance().getModel(model) == null) {
            client.send(new MotdNotificationComposer("Invalid room model"));
            return;
        }

        int roomId = RoomManager.getInstance().createRoom(name, description, model, category, maxVisitors, tradeState, client);
        client.send(new CreateRoomMessageComposer(roomId, name));

        client.getPlayer().setLastRoomCreated((int) Comet.getTime());
    }
}
