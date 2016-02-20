package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Pathfinder;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.types.ItemPathfinder;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerCollision;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.utilities.RandomInteger;

import java.util.List;


public class WiredActionChase extends WiredActionItem {
    private int targetId = -1;

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
    public WiredActionChase(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 8;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (this.getWiredData().getSelectedIds().size() == 0) return false;

        for (long itemId : this.getWiredData().getSelectedIds()) {
            RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

            if (floorItem == null) continue;

            PlayerEntity nearestEntity = floorItem.nearestPlayerEntity();
            Position positionFrom = floorItem.getPosition().copy();

            if (nearestEntity != null) {
                if (this.isCollided(nearestEntity, floorItem)) {
                    if (floorItem.getCollision() != nearestEntity) {
                        floorItem.setCollision(nearestEntity);

                        WiredTriggerCollision.executeTriggers(nearestEntity);
                    }

                    continue;
                }

                this.targetId = nearestEntity.getId();
                List<Square> tilesToEntity = ItemPathfinder.getInstance().makePath(floorItem, nearestEntity.getPosition(), Pathfinder.DISABLE_DIAGONAL, false);

                if (tilesToEntity != null && tilesToEntity.size() != 0) {
                    Position positionTo = new Position(tilesToEntity.get(0).x, tilesToEntity.get(0).y);

                    this.moveToTile(floorItem, positionFrom, positionTo);
                    tilesToEntity.clear();
                } else {
                    this.moveToTile(floorItem, positionFrom, null);
                }
            } else {
                this.moveToTile(floorItem, positionFrom, null);
            }
        }

        return true;
    }

    public boolean isCollided(PlayerEntity entity, RoomItemFloor floorItem) {
        boolean tilesTouching = entity.getPosition().touching(floorItem.getPosition());

        if (tilesTouching) {
            final boolean xMatches = entity.getPosition().getX() == floorItem.getPosition().getX();
            final boolean yMatches = entity.getPosition().getY() == floorItem.getPosition().getY();

            if (!xMatches && !yMatches) {
                return false;
            }

            return true;
        }

        return false;
    }

    private void moveToTile(RoomItemFloor floorItem, Position from, Position to) {
        if (to == null) {
            for (int i = 0; i < 16; i++) {
                if (to != null) break;

                to = this.random(floorItem, from);
            }

            if (to == null) return;
        }

        if (this.getRoom().getItems().moveFloorItem(floorItem.getId(), to, floorItem.getRotation(), true)) {
            to.setZ(floorItem.getPosition().getZ());

            this.getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(from, to, 0, 0, floorItem.getVirtualId()));
        }

        floorItem.nullifyCollision();
    }

    private Position random(RoomItemFloor floorItem, Position from) {
        int randomDirection = RandomInteger.getRandom(0, 3) * 2;
        Position newPosition = from.squareInFront(randomDirection);
        RoomTile tile = floorItem.getRoom().getMapping().getTile(newPosition.getX(), newPosition.getY());

        if (tile != null && tile.isReachable(floorItem)) {
            return newPosition;
        }

        return null;
    }

    public int getTargetId() {
        return this.targetId;
    }
}
