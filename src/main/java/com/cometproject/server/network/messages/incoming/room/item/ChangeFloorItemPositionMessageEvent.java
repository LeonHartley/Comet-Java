package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.TileInstance;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ChangeFloorItemPositionMessageEvent implements IEvent {
    private static Logger log = Logger.getLogger(ChangeFloorItemPositionMessageEvent.class);

    public void handle(Session client, Event msg) {
        int id = msg.readInt();
        int x = msg.readInt();
        int y = msg.readInt();
        int rot = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());

        if ((isOwner || hasRights) || client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            try {
                RoomItemFloor item = room.getItems().getFloorItem(id);
                TileInstance tile = room.getMapping().getTile(x, y);

                if(!tile.canStack() && tile.getTopItem() != item.getId()) {
                    client.send(UpdateFloorItemMessageComposer.compose(item, room.getData().getOwnerId()));
                    return;
                }

                double height = item.getId() == tile.getTopItem() ? item.getHeight() : tile.getStackHeight();

                List<RoomItemFloor> floorItemsAt = room.getItems().getItemsOnSquare(x, y);

                for (RoomItemFloor stackItem : floorItemsAt) {
                    if (item.getId() != stackItem.getId()) {
                        stackItem.onItemAddedToStack(item);
                    }
                }

                List<GenericEntity> affectEntities0 = room.getEntities().getEntitiesAt(item.getX(), item.getY());

                for (GenericEntity entity0 : affectEntities0) {
                    item.onEntityStepOff(entity0);
                }

                List<Position3D> tilesToUpdate = new ArrayList<>();

                tilesToUpdate.add(new Position3D(item.getX(), item.getY(), item.getHeight()));
                tilesToUpdate.add(new Position3D(x, y, item.getHeight()));

                // Catch this so the item still updates!
                try {
                    for (AffectedTile affectedTile : AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), item.getX(), item.getY(), item.getRotation())) {
                        tilesToUpdate.add(new Position3D(affectedTile.x, affectedTile.y, 0d));

                        List<GenericEntity> affectEntities1 = room.getEntities().getEntitiesAt(affectedTile.x, affectedTile.y);

                        for (GenericEntity entity1 : affectEntities1) {
                            item.onEntityStepOff(entity1);
                        }
                    }

                    for (AffectedTile affectedTile : AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), x, y, item.getRotation())) {
                        tilesToUpdate.add(new Position3D(affectedTile.x, affectedTile.y, 0d));

                        List<GenericEntity> affectEntities2 = room.getEntities().getEntitiesAt(affectedTile.x, affectedTile.y);

                        for (GenericEntity entity2 : affectEntities2) {
                            item.onEntityStepOn(entity2);
                        }
                    }
                } catch (Exception e) {
                    log.error("Failed to update entity positions for changing item position", e);
                }

                item.setX(x);
                item.setY(y);

                item.setHeight(height);
                item.setRotation(rot);

                List<GenericEntity> affectEntities3 = room.getEntities().getEntitiesAt(x, y);

                for (GenericEntity entity3 : affectEntities3) {
                    item.onEntityStepOn(entity3);
                }

                RoomItemDao.saveItemPosition(x, y, height, rot, id);

                room.getEntities().broadcastMessage(UpdateFloorItemMessageComposer.compose(item, room.getData().getOwnerId()));

                for (Position3D tileToUpdate : tilesToUpdate) {
                    room.getMapping().updateTile(tileToUpdate.getX(), tileToUpdate.getY());
                }
            } catch (Exception e) {
                log.fatal("Error whilst changing floor item position!", e);
            }
        }
    }
}