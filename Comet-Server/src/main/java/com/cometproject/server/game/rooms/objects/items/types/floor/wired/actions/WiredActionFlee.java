package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.types.ItemPathfinder;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;

import java.util.Iterator;


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
    public WiredActionFlee(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
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
    public boolean evaluate(RoomEntity entity, Object data) {
        return false;
    }


//    public boolean evaluate(RoomEntity entity, Object data) {
//        if (getWiredData().getSelectedIds().size() == 0) {
//            return false;
//        }
//        for (Iterator localIterator = getWiredData().getSelectedIds().iterator(); localIterator.hasNext(); ) {
//            long itemId = ((Long) localIterator.next()).longValue();
//            RoomItemFloor floorItem = getRoom().getItems().getFloorItem(itemId);
//
//            if (floorItem == null) {
//                getWiredData().getSelectedIds().remove(Long.valueOf(itemId));
//            } else {
//                PlayerEntity nearestEntity = floorItem.nearestPlayerEntity();
//                Position positionFrom = floorItem.getPosition().copy();
//
//                if (nearestEntity != null) {
//                    Position newCoordinate = getRoom().getMapping().squareBehind(floorItem.getPosition(), nearestEntity.getPosition());
//
//                    List<Square> tilesToEntity = ItemPathfinder.getInstance().makePath(floorItem, newCoordinate, (byte) 0, false);
//                    if ((tilesToEntity != null) && (tilesToEntity.size() != 0)) {
//                        Position positionTo = new Position(((Square) tilesToEntity.get(0)).x, ((Square) tilesToEntity.get(0)).y);
//                        moveToTile(floorItem, positionFrom, positionTo);
//                        tilesToEntity.clear();
//                    } else {
//                        moveToTile(floorItem, positionFrom, null);
//                    }
//                } else {
//                    moveToTile(floorItem, positionFrom, null);
//                }
//            }
//        }
//        return true;
//    }
//
//    public boolean isCollided(PlayerEntity entity, RoomItemFloor floorItem) {
//        boolean tilesTouching = entity.getPosition().touching(floorItem.getPosition());
//
//        if (tilesTouching) {
//            boolean xMatches = entity.getPosition().getX() == floorItem.getPosition().getX();
//            boolean yMatches = entity.getPosition().getY() == floorItem.getPosition().getY();
//
//            if ((!xMatches) && (!yMatches)) {
//                return false;
//            }
//
//            return true;
//        }
//
//        return false;
//    }
//
//    private void moveToTile(RoomItemFloor floorItem, Position from, Position to) {
//        if (from == null) {
//            return;
//        }
//        if (to == null) {
//            for (int i = 0; i < 16; i++) {
//                if (to != null)
//                    break;
//                to = random(floorItem, from);
//            }
//
//            if (to == null) {
//                return;
//            }
//        }
//        RoomTile tile = getRoom().getMapping().getTile(to.getX(), to.getY());
//        RoomItemFloor onMap = getRoom().getItems().getFloorItem(tile.getTopItem());
//        double height = (onMap != null) && (onMap.getId() != floorItem.getId()) ? onMap.getPosition().getZ() + onMap.getDefinition().getHeight() : 0.0D;
//
//        if (getRoom().getItems().moveFloorItem(floorItem.getId(), to, height, floorItem.getRotation(), true)) {
//            getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(from, to, height, 0, 0, floorItem.getVirtualId()));
//        }
//
//        PlayerEntity nearestEntity = floorItem.nearestPlayerEntity();
//        if ((nearestEntity != null) &&
//                (isCollided(nearestEntity, floorItem))) {
//            floorItem.setCollision(nearestEntity);
//            WiredTriggerCollision.executeTriggers(nearestEntity);
//        }
//
//        floorItem.nullifyCollision();
//    }
//
//    private Position random(RoomItemFloor floorItem, Position from) {
//        int randomDirection = com.cometproject.server.utilities.RandomInteger.getRandom(0, 3) * 2;
//        Position newPosition = from.squareBehind(randomDirection);
//        RoomTile tile = floorItem.getRoom().getMapping().getTile(newPosition.getX(), newPosition.getY());
//
//        if ((tile != null) && (tile.isReachable(floorItem))) {
//            return newPosition;
//        }
//
//        return null;
//    }
//
//    public int getTargetId() {
//        return this.targetId;
//    }
}
