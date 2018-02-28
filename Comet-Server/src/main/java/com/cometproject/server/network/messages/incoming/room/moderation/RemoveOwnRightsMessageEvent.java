package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.permissions.YouAreControllerMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class RemoveOwnRightsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (client.getPlayer().getEntity() == null) {
            return;
        }

        if (client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId())) {
            client.getPlayer().getEntity().getRoom().getRights().removeRights(client.getPlayer().getId());
            RoomManager.getInstance().rightsRoomsUpdate(client);

            client.send(new YouAreControllerMessageComposer(0));

            client.getPlayer().getEntity().removeStatus(RoomEntityStatus.CONTROLLER);
            client.getPlayer().getEntity().addStatus(RoomEntityStatus.CONTROLLER, "0");
            client.getPlayer().getEntity().markNeedsUpdate();
        }
    }
}
