package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.utilities.DistanceCalculator;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

import java.util.ArrayList;
import java.util.List;

public abstract class RollableFloorItem extends RoomItemFloor {
    private boolean isRolling = false;

    private List<Position> rollingPositions;

    public RollableFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityPreStepOn(GenericEntity entity) {
        if (this.isRolling) {
            return;
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
        for(RoomItemFloor floorItem : this.getRoom().getItems().getItemsOnSquare(newPosition.getX(), newPosition.getY())) {
            floorItem.onItemAddedToStack(this);
        }

        this.getPosition().setZ(newPosition.getZ());

        this.isRolling = false;

        RoomItemDao.saveItemPosition(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), this.getRotation(), this.getId());
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

    public static final int KICK_POWER = 6;

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTriggered) {
        if (this.isRolling) {
            return;
        }

        if (!DistanceCalculator.tilesTouching(this.getPosition().getX(), this.getPosition().getY(), entity.getPosition().getX(), entity.getPosition().getY())) {
            return;
        }

        this.setRotation(entity.getBodyRotation());

        List<Position> positions = new ArrayList<>();
        Position currentPosition = new Position(this.getPosition().getX(), this.getPosition().getY());
        Position nextPosition = currentPosition.squareInFront(entity.getBodyRotation());

        boolean needsReverse = false;

        for (int i = 0; i < KICK_POWER; i++) {
            if (!this.getRoom().getMapping().isValidStep(currentPosition, nextPosition, false, true) || !this.getRoom().getEntities().isSquareAvailable(nextPosition.getX(), nextPosition.getY())) {
                needsReverse = true;
                nextPosition = nextPosition.squareBehind(this.getRotation());
                continue;
            }

            if (i > 1)
                positions.add(nextPosition);

            if (needsReverse) {
                nextPosition = nextPosition.squareBehind(this.getRotation());
            } else {
                nextPosition = nextPosition.squareInFront(this.getRotation());
            }
        }

        this.setRollingPositions(positions);

        this.setTicks(RoomItemFactory.getProcessTime(0.5));
    }

    @Override
    public void onTickComplete() {
        Position newPosition = this.rollingPositions.get(0);
        Position currentPosition = new Position(this.getPosition().getX(), this.getPosition().getY());

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

        if (this.rollingPositions.size() != 0)
            this.setTicks(RoomItemFactory.getProcessTime(0.5));

        // tell all other items on the new square that there's a new item. (good method of updating score...)
        for(RoomItemFloor floorItem : this.getRoom().getItems().getItemsOnSquare(newPosition.getX(), newPosition.getY())) {
            floorItem.onItemAddedToStack(this);
        }

        RoomItemDao.saveItemPosition(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), this.getRotation(), this.getId());
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
}
