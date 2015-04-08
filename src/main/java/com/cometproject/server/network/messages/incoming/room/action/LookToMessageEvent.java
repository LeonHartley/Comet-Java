package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class LookToMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        PlayerEntity avatar = client.getPlayer().getEntity();

        if (avatar == null) {
            return;
        }

        int x = msg.readInt();
        int y = msg.readInt();

        if (x == avatar.getPosition().getX() && y == avatar.getPosition().getY())
            return;

        int rotation = Position.calculateRotation(avatar.getPosition().getX(), avatar.getPosition().getY(), x, y, false);

        avatar.unIdle();

        if (!avatar.hasStatus(RoomEntityStatus.SIT) && !avatar.hasStatus(RoomEntityStatus.LAY)) {
            avatar.setBodyRotation(rotation);
            avatar.setHeadRotation(rotation);

            avatar.markNeedsUpdate();
        }
    }
}
