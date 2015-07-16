package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class RemoveOwnRightsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (client.getPlayer().getEntity() == null) {
            return;
        }

        if (client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId())) {
            client.getPlayer().getEntity().getRoom().getRights().removeRights(client.getPlayer().getId());

            client.send(new AccessLevelMessageComposer(0));

            client.getPlayer().getEntity().removeStatus(RoomEntityStatus.CONTROLLER);
            client.getPlayer().getEntity().addStatus(RoomEntityStatus.CONTROLLER, "0");
            client.getPlayer().getEntity().markNeedsUpdate();
        }
    }
}
