package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastList;

import java.util.List;

public class ChangeFloorItemStateMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();

        if(client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        FloorItem item = room.getItems().getFloorItem(itemId);

        if(item == null) {
            return;
        }

        // Can't close gate when a user is on same tile?
        if (item.getDefinition().getInteraction().equals("gate")) {
            if(room.getEntities().getEntitiesAt(item.getX(), item.getY()).size() > 0) {
                return;
            }

            for(GenericEntity entity : room.getEntities().getEntitiesCollection().values()) {
                if(Position3D.distanceBetween(client.getPlayer().getEntity().getPosition(), new Position3D(item.getX(), item.getY(), 0d)) <= 1 && entity.isWalking()) {
                    return;
                }
            }
        }

        GameEngine.getItems().getInteractions().onInteract(0, item, client.getPlayer().getEntity());

        List<Position3D> tilesToUpdate = new FastList<>();
        tilesToUpdate.add(new Position3D(item.getX(), item.getY(), 0d));

        for (AffectedTile tile : AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), item.getX(), item.getY(), item.getRotation()))
        {
            tilesToUpdate.add(new Position3D(tile.x, tile.y, 0d));
        }

        for(Position3D tileToUpdate : tilesToUpdate) {
            room.getMapping().updateTile(tileToUpdate.getX(), tileToUpdate.getY());
        }
    }
}
