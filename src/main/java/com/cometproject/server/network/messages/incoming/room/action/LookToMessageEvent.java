package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class LookToMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        PlayerEntity avatar = client.getPlayer().getEntity();

        if(avatar == null) {
            return;
        }

        GenericEntity lookingAt = avatar.getRoom().getEntities().getEntity(msg.readInt());

        if(lookingAt == null || avatar == lookingAt) {
            return;
        }

        int rotation = Position3D.calculateRotation(avatar.getPosition().getX(), avatar.getPosition().getY(), lookingAt.getPosition().getX(), lookingAt.getPosition().getY(), false);

        avatar.unIdle();

        if(!avatar.hasStatus("sit"))
            avatar.setBodyRotation(rotation);

        avatar.setHeadRotation(rotation);

        avatar.markNeedsUpdate();
    }
}
