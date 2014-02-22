package com.cometproject.network.messages.incoming.navigator;

import com.cometproject.game.GameEngine;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.misc.MotdNotificationComposer;
import com.cometproject.network.messages.outgoing.navigator.CreateRoomMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class CreateRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String name = msg.readString();
        String model = msg.readString();

        if(GameEngine.getRooms().getModel(model) == null) {
            client.send(MotdNotificationComposer.compose("Invalid room model"));
            return;
        }

        int roomId = GameEngine.getRooms().createRoom(name, model, client);

        client.send(CreateRoomMessageComposer.compose(roomId, name));
    }
}
