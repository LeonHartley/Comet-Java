package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

import java.util.ArrayList;
import java.util.List;

public class  ChangeFloorItemPositionMessageEvent implements IEvent {
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
                FloorItem item = room.getItems().getFloorItem(id);

                float height = (float) client.getPlayer().getEntity().getRoom().getModel().getSquareHeight()[x][y];

                for (FloorItem stackItem : room.getItems().getItemsOnSquare(x, y)) {
                    if (item.getId() != stackItem.getId()) {
                        if (stackItem.getDefinition().canStack) {
                            height += stackItem.getDefinition().getHeight();
                            //height += stackItem.getHeight() + stackItem.getDefinition().getHeight();
                        } else {
                            // Hacky fix to send feedback to player if they can't move their item to new position :P
                            client.send(UpdateFloorItemMessageComposer.compose(item, room.getData().getOwnerId()));
                            return;
                        }
                    }
                }

                List<GenericEntity> affectEntities = room.getEntities().getEntitiesAt(item.getX(), item.getY());

                for (GenericEntity entity : affectEntities) {
                    if (entity.hasStatus("sit")) {
                        entity.removeStatus("sit");

                        entity.markNeedsUpdate();
                    }
                }

                List<Position3D> tilesToUpdate = new ArrayList<>();

                tilesToUpdate.add(new Position3D(item.getX(), item.getY(), item.getHeight()));
                tilesToUpdate.add(new Position3D(x, y, item.getHeight()));

                for (AffectedTile affectedTile : AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), item.getX(), item.getY(), item.getRotation())) {
                    tilesToUpdate.add(new Position3D(affectedTile.x, affectedTile.y, 0d));
                    List<GenericEntity> affectEntities0 = room.getEntities().getEntitiesAt(affectedTile.x, affectedTile.y);

                    for (GenericEntity entity0 : affectEntities0) {
                        if (entity0.hasStatus("sit")) {
                            entity0.removeStatus("sit");

                            entity0.markNeedsUpdate();
                        }
                    }
                }

                item.setX(x);
                item.setY(y);

                item.setRotation(rot);
                item.setHeight(height);

                List<GenericEntity> newAffectEntities0 = room.getEntities().getEntitiesAt(item.getX(), item.getY());

                for (GenericEntity entity0 : newAffectEntities0) {
                    if (!entity0.hasStatus("sit") && item.getDefinition().canSit) {
                        entity0.addStatus("sit", String.valueOf(item.getDefinition().getHeight()));
                        entity0.setBodyRotation(rot);
                        entity0.setHeadRotation(rot);
                        entity0.markNeedsUpdate();
                    }
                }

                for (AffectedTile affectedTile : AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), item.getX(), item.getY(), item.getRotation())) {
                    tilesToUpdate.add(new Position3D(affectedTile.x, affectedTile.y, 0d));

                    List<GenericEntity> affectEntities0 = room.getEntities().getEntitiesAt(affectedTile.x, affectedTile.y);

                    for (GenericEntity entity0 : affectEntities0) {
                        if (!entity0.hasStatus("sit") && item.getDefinition().canSit) {
                            entity0.addStatus("sit", String.valueOf(item.getDefinition().getHeight()));
                            entity0.setBodyRotation(rot);
                            entity0.setHeadRotation(rot);

                            entity0.markNeedsUpdate();
                        }
                    }
                }

                RoomItemDao.saveItemPosition(x, y, height, rot, id);

                room.getItems().getFloorItems().remove(item);
                room.getItems().getFloorItems().add(item);
                room.getEntities().broadcastMessage(UpdateFloorItemMessageComposer.compose(item, room.getData().getOwnerId()));

                for (Position3D tileToUpdate : tilesToUpdate) {
                    room.getMapping().updateTile(tileToUpdate.getX(), tileToUpdate.getY());
                }
            } catch (Exception e) {
                room.log.error("Error while changing floor item position", e);
            }
        }
    }
}
