package com.cometsrv.network.messages.incoming.room.engine;

import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometsrv.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometsrv.network.messages.outgoing.room.avatar.DanceMessageComposer;
import com.cometsrv.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometsrv.network.messages.outgoing.room.engine.RoomPanelMessageComposer;
import com.cometsrv.network.messages.outgoing.room.permissions.FloodFilterMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class AddUserToRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        PlayerEntity avatar = client.getPlayer().getEntity();

        if(avatar == null) {
            return;
        }

        Room room = avatar.getRoom();

        if(room == null) {
            return;
        }

        // TODO: Check this over

        if(!room.getProcess().isActive()) {
            room.getProcess().start();
        }

        if(!room.getItemProcess().isActive()) {
            room.getItemProcess().start();
        }

        if(client.getPlayer().floodTime >= 1) {
            client.send(FloodFilterMessageComposer.compose(client.getPlayer().floodTime));
        }

        client.send(RoomPanelMessageComposer.compose(room.getId(), room.getRights().hasRights(client.getPlayer().getId()) || room.getData().getOwnerId() == client.getPlayer().getId()));
        client.send(RoomDataMessageComposer.compose(room));

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(AvatarsMessageComposer.compose(client.getPlayer().getEntity().getRoom()));
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(AvatarUpdateMessageComposer.compose(client.getPlayer().getEntity().getRoom()));

        for(GenericEntity av : client.getPlayer().getEntity().getRoom().getEntities().getEntitiesCollection().values()) {
            if(av.getDanceId() != 0) {
                client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(DanceMessageComposer.compose(av.getVirtualId(), av.getDanceId()));
            }
        }
    }
}
