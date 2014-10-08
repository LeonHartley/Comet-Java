package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;
import java.util.List;

public class ChangeFloorItemStateMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        RoomItemFloor item = room.getItems().getFloorItem(itemId);

        if (item == null) {
            return;
        }

        // Can't close gate when a user is on same tile?
        /*if (item.getDefinition().getInteraction().equals("gate")) {
            for (AffectedTile tile : AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), item.getPosition().getX(), item.getPosition().getY(), item.getRotation())) {
                if (room.getEntities().getEntitiesAt(tile.x, tile.y).size() > 0) {
                    return;
                }
            }

            if (room.getEntities().getEntitiesAt(item.getPosition().getX(), item.getPosition().getY()).size() > 0) {
                return;
            }

            for (GenericEntity entity : room.getEntities().getAllEntities().values()) {
                if (Position3D.distanceBetween(client.getPlayer().getEntity().getPosition(), new Position3D(item.getPosition().getX(), item.getPosition().getY(), 0d)) <= 1 && entity.isWalking()) {
                    return;
                }
            }
        }*/

        item.onInteract(client.getPlayer().getEntity(), msg.readInt(), false);

        // to-do: move below into each onInteract or turn onInteract into a boolean (i prefer the latter) no biggie for now

        List<Position> tilesToUpdate = new ArrayList<>();
        tilesToUpdate.add(new Position(item.getPosition().getX(), item.getPosition().getY(), 0d));

        for (AffectedTile tile : AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), item.getPosition().getX(), item.getPosition().getY(), item.getRotation())) {
            if (room.getEntities().getEntitiesAt(tile.x, tile.y).size() >= 0)
                tilesToUpdate.add(new Position(tile.x, tile.y, 0d));
        }

        for (Position tileToUpdate : tilesToUpdate) {
            room.getMapping().updateTile(tileToUpdate.getX(), tileToUpdate.getY());
        }
    }
}
