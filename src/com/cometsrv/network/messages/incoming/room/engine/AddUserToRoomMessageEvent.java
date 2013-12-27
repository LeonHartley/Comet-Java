package com.cometsrv.network.messages.incoming.room.engine;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.RoomModel;
import com.cometsrv.game.wired.types.TriggerType;
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
        Avatar avatar = client.getPlayer().getAvatar();

        if(avatar == null) {
            return;
        }

        Room room = avatar.getRoom();

        if(room == null) {
            return;
        }

        RoomModel model = room.getModel();
        avatar.getPosition().setX(model.getDoorX());
        avatar.getPosition().setY(model.getDoorY());
        avatar.getPosition().setZ(model.getDoorZ());

        avatar.setBodyRotation(model.getDoorRotation());
        avatar.setHeadRotation(model.getDoorRotation());

        room.getAvatars().getAvatars().put(avatar.getPlayer().getId(), avatar);

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

        client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(AvatarsMessageComposer.compose(client.getPlayer().getAvatar().getRoom()));
        client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(AvatarUpdateMessageComposer.compose(client.getPlayer().getAvatar().getRoom()));

        for(Avatar av : client.getPlayer().getAvatar().getRoom().getAvatars().getAvatars().values()) {
            if(av.getDanceId() != 0) {
                client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(DanceMessageComposer.compose(av.getPlayer().getId(), av.getDanceId()));
            }
        }
    }
}
