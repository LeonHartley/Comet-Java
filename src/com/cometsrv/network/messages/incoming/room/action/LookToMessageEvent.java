package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.avatars.misc.Position;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class LookToMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        Avatar avatar = client.getPlayer().getAvatar();

        if(avatar == null) return;
        Avatar lookingAt = avatar.getRoom().getAvatars().get(msg.readInt());

        if(lookingAt == null || avatar == lookingAt) return;

        int rotation = Position.calculateRotation(avatar.getPosition().getX(), avatar.getPosition().getY(), lookingAt.getPosition().getX(), lookingAt.getPosition().getY(), false);

        avatar.unidle();
        avatar.setBodyRotation(rotation);
        avatar.setHeadRotation(rotation);
        avatar.setNeedsUpdate(true);
    }
}
