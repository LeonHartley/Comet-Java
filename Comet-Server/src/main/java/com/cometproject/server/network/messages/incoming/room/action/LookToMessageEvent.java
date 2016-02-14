package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class LookToMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        PlayerEntity avatar = client.getPlayer().getEntity();

        if (avatar == null) {
            return;
        }

        if(!client.getPlayer().getEntity().isVisible()) {
            return;
        }

        int x = msg.readInt();
        int y = msg.readInt();

        if(avatar.getMountedEntity() != null) {
            return;
        }

        avatar.lookTo(x, y);
    }
}
