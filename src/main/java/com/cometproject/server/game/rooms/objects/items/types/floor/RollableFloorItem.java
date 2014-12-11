package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.banzai.BanzaiPuckFloorItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.utilities.DistanceCalculator;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.cometproject.server.utilities.Direction;
import com.google.common.collect.Lists;

import java.util.List;


public abstract class RollableFloorItem extends RoomItemFloor {
    public static final int KICK_POWER = 6;

    private boolean isRolling = false;
    private List<Position> rollingPositions;

    private PlayerEntity playerEntity;
    private boolean skipNext = false;
    private boolean isDribbling = false;

    public RollableFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityPreStepOn(GenericEntity entity) {
        if (this.isRolling) {
            return;
        }

        if (entity instanceof PlayerEntity && this instanceof BanzaiPuckFloorItem) {
            this.setExtraData((((PlayerEntity) entity).getGameTeam().getTeamId() + 1) + "");
            this.sendUpdate();
        }

        if (entity.getWalkingGoal().getX() == this.getPosition().getX() && entity.getWalkingGoal().getY() == this.getPosition().getY()) {
            if (this.skipNext) {
                if (isDribbling) {
                    this.onInteract(entity, 0, false);
                    this.isDribbling = false;
                }

                this.skipNext = false;
                return;
            }

            if (entity instanceof PlayerEntity) {
                this.playerEntity = (PlayerEntity) entity;
            }

            this.rollBall(entity.getPosition(), KICK_POWER, entity.getBodyRotation());
        } else {
            if (entity.getWalkingGoal().distanceTo(this.getPosition()) > 1) {
                this.isDribbling = true;
            }

            this.skipNext = true;
            this.onInteract(entity, 0, false);
        }
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        this.rollBall(this.getPosition(), KICK_POWER, Direction.get(entity.getBodyRotation()).invert().num);
    }

    private void rollBall(Position from, int power, int rotation) {
        if (!DistanceCalculator.tilesTouching(this.getPosition().getX(), this.getPosition().getY(), from.getX(), from.getY())) {
            return;
        }

        this.setRotation(rotation);

        List<Position> positions = Lists.newArrayList();
        Position currentPosition = new Position(this.getPosition().getX(), this.getPosition().getY());
        Position nextPosition = currentPosition.squareInFront(rotation);

        boolean needsReverse = false;

        for (int i = 0; i < power; i++) {
            if (!this.getRoom().getMapping().isValidStep(currentPosition, nextPosition, false, true) || !this.getRoom().getEntities().isSquareAvailable(nextPosition.getX(), nextPosition.getY())) {
                needsReverse = true;
                nextPosition = nextPosition.squareBehind(this.getRotation());
                continue;
            }

//            if (!positions.isEmpty() && positions.get(positions.size() - 1).getX() == nextPosition.getX() && positions.get(positions.size() - 1).getY() == nextPosition.getY()) {
//                nextPosition = nextPosition.squareBehind(this.getRotation());
//                i--;
//                continue;
//            } else {
            positions.add(nextPosition);
//            }

            if (needsReverse) {
                nextPosition = nextPosition.squareBehind(this.getRotation());
            } else {
                nextPosition = nextPosition.squareInFront(this.getRotation());
            }
        }

        this.setRollingPositions(positions);

        this.setTicks(RoomItemFactory.getProcessTime(0.075));
    }

    public static void roll(RoomItemFloor item, Position from, Position to, Room room) {
        room.getEntities().broadcastMessage(
                SlideObjectBundleMessageComposer.compose(
                        from,
                        to, item.getId(), 0, item.getId())
        );
    }

    public static Position calculatePosition(int x, int y, int playerRotation) {
        return calculatePosition(x, y, playerRotation, false);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTriggered) {
        if (isWiredTriggered) return;

        if (this.isRolling || !entity.getPosition().touching(this.getPosition())) {
            return;
        }

        if (entity instanceof PlayerEntity) {
            this.playerEntity = (PlayerEntity) entity;
        }

        this.isRolling = true;

        Position currentPosition = new Position(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ());

        Position newPosition;

        if (entity.getRoom().getMapping().isValidStep(currentPosition, calculatePosition(this.getPosition().getX(), this.getPosition().getY(), entity.getBodyRotation()), false)) {
            newPosition = calculatePosition(this.getPosition().getX(), this.getPosition().getY(), entity.getBodyRotation());
        } else {
            newPosition = calculatePosition(this.getPosition().getX(), this.getPosition().getY(), entity.getBodyRotation(), true);
        }

        newPosition.setZ(this.getRoom().getModel().getSquareHeight()[newPosition.getX()][newPosition.getY()]);

        roll(this, currentPosition, newPosition, entity.getRoom());

        this.setRotation(entity.getBodyRotation());
        this.getPosition().setX(newPosition.getX());
        this.getPosition().setY(newPosition.getY());

        // tell all other items on the new square that there's a new item. (good method of updating score...)
        for (RoomItemFloor floorItem : this.getRoom().getItems().getItemsOnSquare(newPosition.getX(), newPosition.getY())) {
            floorItem.onItemAddedToStack(this);
        }

        this.getPosition().setZ(newPosition.getZ());

        this.isRolling = false;

        RoomItemDao.saveItemPosition(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), this.getRotation(), this.getId());
    }

    private int rollCount = 0;

    @Override
    public void onTickComplete() {
        this.rollCount++;

        if (this.rollingPositions.isEmpty()) return;

        Position newPosition = this.rollingPositions.get(0);
        Position currentPosition = new Position(this.getPosition().getX(), this.getPosition().getY());

//        if(newPosition.getX() == currentPosition.getX() && newPosition.getY() == currentPosition.getY()) {
//
//        }

        currentPosition.setZ(this.getRoom().getModel().getSquareHeight()[currentPosition.getX()][currentPosition.getY()]);
        newPosition.setZ(this.getRoom().getModel().getSquareHeight()[newPosition.getX()][newPosition.getY()]);

        if (!this.getRoom().getMapping().isValidStep(currentPosition, newPosition, false, true) || !this.getRoom().getEntities().isSquareAvailable(newPosition.getX(), newPosition.getY())) {
            newPosition = currentPosition.squareBehind(this.rotation);
        }

        if (this.getRoom().getMapping().isValidStep(currentPosition, newPosition, false, true)) {
            roll(this, currentPosition, newPosition, this.getRoom());

            this.getPosition().setX(newPosition.getX());
            this.getPosition().setY(newPosition.getY());
            this.getPosition().setZ(newPosition.getZ());
        }

        this.rollingPositions.remove(newPosition);

        if (this.rollingPositions.size() != 0) {
            this.setTicks(RoomItemFactory.getProcessTime(this.getDelay(this.rollCount)));
        } else {
            this.rollCount = 0;
        }


        // tell all other items on the new square that there's a new item. (good method of updating score...)
        for (RoomItemFloor floorItem : this.getRoom().getItems().getItemsOnSquare(newPosition.getX(), newPosition.getY())) {
            floorItem.onItemAddedToStack(this);
        }

        RoomItemDao.saveItemPosition(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), this.getRotation(), this.getId());
    }

    public static Position calculatePosition(int x, int y, int playerRotation, boolean isReversed) {
        switch (playerRotation) {
            case 0:
                if (!isReversed)
                    y--;
                else
                    y++;
                break;

            case 1:
                if (!isReversed) {
                    x++;
                    y--;
                } else {
                    x--;
                    y++;
                }
                break;

            case 2:
                if (!isReversed)
                    x++;
                else
                    x--;
                break;

            case 3:
                if (!isReversed) {
                    x++;
                    y++;
                } else {
                    x--;
                    y--;
                }
                break;

            case 4:
                if (!isReversed)
                    y++;
                else
                    y--;
                break;

            case 5:
                if (!isReversed) {
                    x--;
                    y++;
                } else {
                    x++;
                    y--;
                }
                break;

            case 6:
                if (!isReversed)
                    x--;
                else
                    x++;
                break;

            case 7:
                if (!isReversed) {
                    x--;
                    y--;
                } else {
                    x++;
                    y++;
                }
                break;
        }

        return new Position(x, y);
    }

    private double getDelay(int i) {
        switch (i) {
            case 1:
                return 0.075;
            case 2:
                return 0.1;
            case 3:
                return 0.125;
            case 4:
                return 0.3;
            default:
                if (i != 5) {
                    return ((i != 6) ? 0.2 : 3.5);
                }
                return 0.35;
        }
    }

    public boolean isRolling() {
        return (this.rollingPositions != null && this.rollingPositions.size() > 0);
    }

    public List<Position> getRollingPositions() {
        return this.rollingPositions;
    }

    public void setRollingPositions(List<Position> positions) {
        this.rollingPositions = positions;
    }

    public PlayerEntity getPusher() {
        return playerEntity;
    }
}
