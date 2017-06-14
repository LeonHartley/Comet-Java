package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.items.types.LowPriorityItemProcessor;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Square;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.types.ItemPathfinder;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.banzai.BanzaiPuckFloorItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.game.utilities.DistanceCalculator;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.cometproject.server.utilities.Direction;

import java.util.List;


public abstract class RollableFloorItem extends RoomItemFloor {
    public static final int KICK_POWER = 6;

    private boolean isRolling = false;
    private PlayerEntity playerEntity;
    private boolean skipNext = false;
    private boolean wasDribbling = false;
    private int rollStage = -1;

    public RollableFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    public static void roll(RoomItemFloor item, Position from, Position to, Room room) {
        room.getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(from.copy(), to.copy(), item.getVirtualId(), 0, item.getVirtualId()));
    }

    public static Position calculatePosition(int x, int y, int playerRotation) {
        return Position.calculatePosition(x, y, playerRotation, false);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (this.skipNext && (this.playerEntity != null && entity.getId() == this.playerEntity.getId())) {
            this.skipNext = false;
            return;
        }

        if (entity instanceof PlayerEntity && this instanceof BanzaiPuckFloorItem) {
            this.setExtraData((((PlayerEntity) entity).getGameTeam().getTeamId() + 1) + "");
            this.sendUpdate();
        }

        boolean isOnBall = entity.getWalkingGoal().getX() == this.getPosition().getX() && entity.getWalkingGoal().getY() == this.getPosition().getY();

        if (isOnBall) {
            if (entity instanceof PlayerEntity) {
                this.playerEntity = (PlayerEntity) entity;
            }

            this.wasDribbling = false;
            this.rollStage = 0;
            this.rollBall(entity.getPosition(), entity.getBodyRotation());
//        } else if(isOnBall) {
//            this.wasDribbling = false;
        } else {
            this.rollSingle(entity);
            this.wasDribbling = true;
        }
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (!this.skipNext) {
            this.rollBall(this.getPosition(), Direction.get(entity.getBodyRotation()).invert().num);
        } else {
            this.skipNext = false;
        }
    }

    private void rollBall(Position from, int rotation) {
        if (!DistanceCalculator.tilesTouching(this.getPosition().getX(), this.getPosition().getY(), from.getX(), from.getY())) {
            return;
        }

        this.rotation = rotation;
        this.isRolling = true;
        this.rollStage = 0;

        this.setTicks(LowPriorityItemProcessor.getProcessTime(0.075));
    }

    @Override
    public void onTickComplete() {
        if (!this.isRolling || this.rollStage == -1 || this.rollStage >= KICK_POWER) {
            this.isRolling = false;
            this.rollStage = -1;
            this.skipNext = false;
            this.wasDribbling = false;
            return;
        }

        this.rollStage++;

        Position nextPosition = this.getNextPosition(this.getPosition(), false);

        if (!this.isValidRoll(nextPosition)) {
//            if (this.playerEntity != null) {
//                if (this.playerEntity.getWalkingGoal().equals(nextPosition)) {
//                    this.isRolling = false;
//                    this.rollStage = -1;
//                    return;
//                }
//            }

            nextPosition = this.getNextPosition(this.getPosition(), true);
            this.setRotation(Direction.get(this.getRotation()).invert().num);
        }

        this.moveTo(nextPosition, this.getRotation());
        this.setTicks(LowPriorityItemProcessor.getProcessTime(this.getDelay(this.rollStage)));
    }

    private boolean isValidRoll(Position nextPosition) {
        List<Square> path = ItemPathfinder.getInstance().makePath(this, nextPosition);

        return !(path == null || path.isEmpty());
    }

    private Position getNextPosition(Position nextPosition, boolean needsReverse) {
        Position newPosition;

        if (needsReverse) {
            newPosition = nextPosition.squareBehind(this.getRotation());
        } else {
            newPosition = nextPosition.squareInFront(this.getRotation());
        }

        return newPosition;
    }

    private void rollSingle(RoomEntity entity) {
        if (this.isRolling || !entity.getPosition().touching(this.getPosition())) {
            return;
        }

        if (entity instanceof PlayerEntity) {
            this.playerEntity = (PlayerEntity) entity;

//            if (playerEntity.getBodyRotation() % 2 != 0) {
//                return false;
//            }
        }

        this.isRolling = true;

        Position currentPosition = new Position(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ());

        Position newPosition;

        if (this.isValidRoll(this.getNextPosition(currentPosition, false))) {
            newPosition = calculatePosition(this.getPosition().getX(), this.getPosition().getY(), entity.getBodyRotation());
        } else {
            newPosition = Position.calculatePosition(this.getPosition().getX(), this.getPosition().getY(), entity.getBodyRotation(), true);
            this.setRotation(Direction.get(this.getRotation()).invert().num);
        }

        if (!this.isValidRoll(newPosition)) {
            return;
        }

        this.moveTo(newPosition, entity.getBodyRotation());
        this.isRolling = false;
        this.wasDribbling = false;
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTriggered) {
        if (isWiredTriggered) return false;

        this.rollSingle(entity);
        this.skipNext = true;
        return true;
    }

    @Override
    public void onPositionChanged(Position newPosition) {
        this.isRolling = false;
        this.playerEntity = null;
        this.skipNext = false;
        this.rollStage = -1;
        this.wasDribbling = false;
    }

    private void moveTo(Position pos, int rotation) {
        RoomTile newTile = this.getRoom().getMapping().getTile(pos);

        if (newTile == null) {
            return;
        }

        pos.setZ(newTile.getStackHeight());

        roll(this, this.getPosition(), pos, this.getRoom());

        RoomTile tile = this.getRoom().getMapping().getTile(this.getPosition());

        this.setRotation(rotation);

        this.getPosition().setX(pos.getX());
        this.getPosition().setY(pos.getY());

        if (tile != null) {
            tile.reload();
        }

        newTile.reload();

        // tell all other items on the new square that there's a new item. (good method of updating score...)
        for (RoomItemFloor floorItem : this.getRoom().getItems().getItemsOnSquare(pos.getX(), pos.getY())) {
            floorItem.onItemAddedToStack(this);
        }

        this.getPosition().setZ(pos.getZ());
        RoomItemDao.saveItemPosition(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), this.getRotation(), this.getId());
    }

    private double getDelay(int i) {
        switch (i) {
            case 1:
                return 0.075;
            case 2:
                return 0.4;
            case 3:
                return 0.65;
            case 4:
                return 0.8;
            default:
                if (i != 5) {
                    return ((i != 6) ? 0.95 : 1.1);
                }
                return 1.1;
        }
    }

    private int getRollDirection() {
//        switch(this.rotation) {
//            case 1:
//                return 7;
//
//            case 3:
//                return 6;
//
//            case 5:
//                return 3;
//        }

        return this.rotation;
    }

    public PlayerEntity getPusher() {
        return playerEntity;
    }
}