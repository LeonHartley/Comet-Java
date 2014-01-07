package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class LookToMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        PlayerEntity avatar = client.getPlayer().getEntity();

        if(avatar == null) {
            return;
        }

        System.out.println(msg.readInt());

        PlayerEntity lookingAt = avatar.getRoom().getEntities().tryGetPlayerEntityNullable(msg.readInt());

        if(lookingAt == null || avatar == lookingAt) {
            return;
        }

        int rotation = Position3D.calculateRotation(avatar.getPosition().getX(), avatar.getPosition().getY(), lookingAt.getPosition().getX(), lookingAt.getPosition().getY(), false);

        avatar.unIdle();
        avatar.setBodyRotation(rotation);
        avatar.setHeadRotation(rotation);

        avatar.markNeedsUpdate();
    }
}
