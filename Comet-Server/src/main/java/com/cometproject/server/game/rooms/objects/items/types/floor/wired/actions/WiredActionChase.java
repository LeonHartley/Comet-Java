package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Pathfinder;
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

import java.util.ArrayList;
import java.util.List;


public class WiredActionChase extends WiredActionItem {
    private int targetId = -1;

    /**
     * Wired action to chase room entity
     *
     * @param itemData the item data
     * @param room     the room
     */
    public WiredActionChase(RoomItemData itemData, Room room) {
        super(itemData, room);
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
    public void onEventComplete(WiredItemEvent event) {
        if (this.getWiredData().getSelectedIds().size() == 0) return;

        for (long itemId : this.getWiredData().getSelectedIds()) {
            RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

            if (floorItem == null) continue;

            PlayerEntity nearestEntity = floorItem.nearestPlayerEntity();
            Position positionFrom = floorItem.getPosition().copy();

            if (floorItem.getCollision() != null && this.isCollided(floorItem.getCollision(), floorItem)) {
                WiredTriggerCollision.executeTriggers(nearestEntity, floorItem);
                continue;
            }

            if (nearestEntity != null) {
                if (this.isCollided(nearestEntity, floorItem)) {
                    if (floorItem.getCollision() != nearestEntity) {
                        floorItem.setCollision(nearestEntity);

                        WiredTriggerCollision.executeTriggers(nearestEntity, floorItem);
                    }

                    continue;
                }

                this.targetId = nearestEntity.getId();
                List<Square> tilesToEntity = new ArrayList<>();

                for (int direction : Position.COLLIDE_TILES) {
                    if (tilesToEntity.size() == 0) {
                        tilesToEntity = ItemPathfinder.getInstance().makePath(floorItem, nearestEntity.getPosition().squareInFront(direction), Pathfinder.DISABLE_DIAGONAL, false);
                        break;
                    }
                }

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
    }

    public boolean isCollided(RoomEntity entity, RoomItemFloor floorItem) {
        boolean tilesTouching = entity.getPosition().touching(floorItem.getPosition());

        if (tilesTouching) {
            final boolean xMatches = entity.getPosition().getX() == floorItem.getPosition().getX();
            final boolean yMatches = entity.getPosition().getY() == floorItem.getPosition().getY();

            return xMatches || yMatches;
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

//        floorItem.nullifyCollision();

        for (int collisionDirection : Position.COLLIDE_TILES) {
            final Position collisionPosition = floorItem.getPosition().squareInFront(collisionDirection);
            final RoomTile collisionTile = this.getRoom().getMapping().getTile(collisionPosition);

            if (collisionTile != null) {
                final RoomEntity entity = collisionTile.getEntity();

                if (entity != null) {
                    WiredTriggerCollision.executeTriggers(entity, floorItem);
                }
            }
        }

    }

    private Position random(RoomItemFloor floorItem, Position from) {
        int randomDirection = RandomUtil.getRandomInt(0, 3) * 2;
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
