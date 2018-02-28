package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.api.game.GameContext;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.navigator.CreateRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class CreateRoomMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String name = msg.readString();
        String description = msg.readString();
        String model = msg.readString();
        int category = msg.readInt();
        int maxVisitors = msg.readInt();
        int tradeState = msg.readInt();

        if (client.getPlayer().getRooms().size() >= 100) {
            return;
        }

        int lastRoomCreatedDifference = ((int) Comet.getTime()) - client.getPlayer().getLastRoomCreated();

        if (lastRoomCreatedDifference < 30) {
            client.send(new MotdNotificationMessageComposer(Locale.getOrDefault("room.creation.time", "Due to abuse, you can only create 1 room every 30 seconds (You have " + (30 - lastRoomCreatedDifference) + " seconds left)!")));
            return;
        }

        if (GameContext.getCurrent().getRoomModelService().getModel(model) == null) {
            client.send(new MotdNotificationMessageComposer("Invalid room model"));
            return;
        }

        int roomId = RoomManager.getInstance().createRoom(name, description, model, category, maxVisitors, tradeState, client);

        client.send(new CreateRoomMessageComposer(roomId, name));
        client.getPlayer().setLastRoomCreated((int) Comet.getTime());
    }
}
