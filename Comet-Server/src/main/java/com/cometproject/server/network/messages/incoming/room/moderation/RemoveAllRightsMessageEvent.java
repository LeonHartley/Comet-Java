package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.permissions.RemoveRightsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.YouAreControllerMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

import java.util.ArrayList;
import java.util.List;


public class RemoveAllRightsMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null) {
            return;
        }

        if (room.getData().getOwnerId() != client.getPlayer().getId()) {
            return;
        }

        List<Integer> toRemove = new ArrayList<>();

        for (Integer id : room.getRights().getAll()) {
            PlayerEntity playerEntity = room.getEntities().getEntityByPlayerId(id);

            if (playerEntity != null) {
                playerEntity.getPlayer().getSession().send(new YouAreControllerMessageComposer(0));
            }

            // Remove rights from the player id
            client.send(new RemoveRightsMessageComposer(id, room.getId()));
            toRemove.add(id);
        }

        for (Integer id : toRemove) {
            room.getRights().removeRights(id);

            Session rightsSession = NetworkManager.getInstance().getSessions().getByPlayerId(id);

            if (rightsSession != null) {
                RoomManager.getInstance().rightsRoomsUpdate(rightsSession);
            }
        }

//        client.send(new RightsListMessageComposer(room.getId(), room.getRights().getAll()));
        toRemove.clear();
    }
}
