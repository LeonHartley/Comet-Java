package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.types.ItemPathfinder;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerCollision;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.utilities.RandomUtil;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class WiredActionFlee extends WiredActionItem {

    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredActionFlee(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 12;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (getWiredData().getSelectedIds().size() == 0) {
            return;
        }

        for (Iterator localIterator = getWiredData().getSelectedIds().iterator(); localIterator.hasNext(); ) {
            long itemId = (Long) localIterator.next();
            RoomItemFloor floorItem = getRoom().getItems().getFloorItem(itemId);

            if (floorItem == null) {
                getWiredData().getSelectedIds().remove(itemId);
            } else {
                PlayerEntity nearestEntity = floorItem.nearestPlayerEntity();
                Position positionFrom = floorItem.getPosition().copy();

                if (nearestEntity != null && nearestEntity.getPosition() != null) {
                    Position newCoordinate = floorItem.getPosition().squareBehind(Position.calculateRotation(floorItem.getPosition().getX(), floorItem.getPosition().getY(), nearestEntity.getPosition().getX(), nearestEntity.getPosition().getY(), false));

                    List<Square> tilesToEntity = ItemPathfinder.getInstance().makePath(floorItem, newCoordinate, (byte) 0, false);
                    if ((tilesToEntity != null) && (tilesToEntity.size() != 0)) {
                        Position positionTo = new Position(tilesToEntity.get(0).x, tilesToEntity.get(0).y);
                        moveToTile(floorItem, positionFrom, positionTo);
                        tilesToEntity.clear();
                    } else {
                        moveToTile(floorItem, positionFrom, null);
                    }
                } else {
                    moveToTile(floorItem, positionFrom, null);
                }
            }
        }
    }

    public boolean isCollided(PlayerEntity entity, RoomItemFloor floorItem) {
        boolean tilesTouching = entity.getPosition().touching(floorItem.getPosition());

        if (tilesTouching) {
            boolean xMatches = entity.getPosition().getX() == floorItem.getPosition().getX();
            boolean yMatches = entity.getPosition().getY() == floorItem.getPosition().getY();

            return !((!xMatches) && (!yMatches));

        }

        return false;
    }

    private void moveToTile(RoomItemFloor floorItem, Position from, Position to) {
        if (from == null) {
            return;
        }
        if (to == null) {
            for (int i = 0; i < 16; i++) {
                if (to != null)
                    break;
                to = random(floorItem, from);
            }

            if (to == null) {
                return;
            }
        }

        if (getRoom().getItems().moveFloorItem(floorItem.getId(), to, floorItem.getRotation(), true)) {
            final Map<Integer, Double> items = Maps.newHashMap();

            items.put(floorItem.getVirtualId(), floorItem.getPosition().getZ());

            getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(from, to, 0, 0, items));
        }


        for (int collisionDirection : Position.COLLIDE_TILES) {
            final Position collisionPosition = floorItem.getPosition().squareInFront(collisionDirection);
            final RoomTile collisionTile = this.getRoom().getMapping().getTile(collisionPosition);

            if (collisionTile != null) {
                final RoomEntity entity = collisionTile.getEntity();

                if (entity != null) {
                    floorItem.setCollision(entity);
                    WiredTriggerCollision.executeTriggers(entity, floorItem);
                }
            }
        }
//
//        PlayerEntity nearestEntity = floorItem.nearestPlayerEntity();
//        if ((nearestEntity != null) &&
//                (isCollided(nearestEntity, floorItem))) {
//            floorItem.setCollision(nearestEntity);
//            WiredTriggerCollision.executeTriggers(nearestEntity, floorItem);
//        }

        floorItem.nullifyCollision();
    }

    private Position random(RoomItemFloor floorItem, Position from) {
        int randomDirection = RandomUtil.getRandomInt(0, 3) * 2;
        Position newPosition = from.squareBehind(randomDirection);
        RoomTile tile = floorItem.getRoom().getMapping().getTile(newPosition.getX(), newPosition.getY());

        if ((tile != null) && (tile.isReachable(floorItem))) {
            return newPosition;
        }

        return null;
    }
}
