package com.cometsrv.network.messages.incoming.room.engine;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class InitalizeRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int id = msg.readInt();
        String password = msg.readString();

        if(client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            client.getPlayer().getEntity().dispose();
            client.getPlayer().setAvatar(null);
        }

        Room room = GameEngine.getRooms().get(id);

        if(room == null) {
            client.send(HotelViewMessageComposer.compose());
            return;
        }

        if (!room.isActive) {
            room.load();
        }

        PlayerEntity playerEntity = room.getEntities().createEntity(client.getPlayer());
        client.getPlayer().setAvatar(playerEntity);

        playerEntity.joinRoom(room, password);
    }
}
