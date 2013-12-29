package com.cometsrv.network.messages.incoming.navigator;

import com.cometsrv.game.GameEngine;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.misc.MotdNotificationComposer;
import com.cometsrv.network.messages.outgoing.navigator.CreateRoomMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

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
