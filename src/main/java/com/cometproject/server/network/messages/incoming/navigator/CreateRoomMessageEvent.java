package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationComposer;
import com.cometproject.server.network.messages.outgoing.navigator.CreateRoomMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class CreateRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String name = msg.readString();
        String model = msg.readString();

        if (CometManager.getRooms().getModel(model) == null) {
            client.send(MotdNotificationComposer.compose("Invalid room model"));
            return;
        }

        int roomId = CometManager.getRooms().createRoom(name, model, client);

        client.send(CreateRoomMessageComposer.compose(roomId, name));
    }
}
