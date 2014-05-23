package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.utilities.DistanceCalculator;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;

import java.util.ArrayList;
import java.util.List;

public final class BallFloorItem extends RoomItemFloor {
    private boolean isRolling = false;

    private List<Position3D> rollingPositions;

    public BallFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityPreStepOn(GenericEntity entity) {
        if (this.isRolling) { return; }
        this.isRolling = true;

        Position3D currentPosition = new Position3D(this.getX(), this.getY(), this.getHeight());

        Position3D newPosition;

        if (entity.getRoom().getMapping().isValidStep(currentPosition, calculatePosition(this.getX(), this.getY(), entity.getBodyRotation()), false)) {
            newPosition = calculatePosition(this.getX(), this.getY(), entity.getBodyRotation());
        } else {
            newPosition = calculatePosition(this.getX(), this.getY(), entity.getBodyRotation(), true);
        }

        newPosition.setZ((float) this.getRoom().getModel().getSquareHeight()[newPosition.getX()][newPosition.getY()]);

        roll(this, currentPosition, newPosition, entity.getRoom());

        this.setRotation(entity.getBodyRotation());
        this.setX(newPosition.getX());
        this.setY(newPosition.getY());

        this.setHeight((float) newPosition.getZ());

        this.setExtraData("2");

        this.isRolling = false;
    }

    public static void roll(RoomItemFloor item, Position3D from, Position3D to, Room room) {
        room.getEntities().broadcastMessage(
                SlideObjectBundleMessageComposer.compose(
                        from,
                        to, item.getId(), 0, item.getId())
        );
    }

    public static Position3D calculatePosition(int x, int y, int playerRotation) {
        return calculatePosition(x, y, playerRotation, false);
    }

    public static Position3D calculatePosition(int x, int y, int playerRotation, boolean isReversed) {
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

        return new Position3D(x, y);
    }

    public static final int KICK_POWER = 4;

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTriggered) {
        if (this.isRolling) { return; }

        if (!DistanceCalculator.tilesTouching(this.getX(), this.getY(), entity.getPosition().getX(), entity.getPosition().getY())) {
            return;
        }

        this.setRotation(entity.getBodyRotation());

        List<Position3D> positions = new ArrayList<>();
        Position3D currentPosition = new Position3D(this.getX(), this.getY());
        Position3D nextPosition = currentPosition.squareInFront(entity.getBodyRotation());

        boolean needsReverse = false;

        for (int i = 0; i < KICK_POWER; i++) {
            if (!this.getRoom().getMapping().isValidStep(currentPosition, nextPosition, false) || !this.getRoom().getEntities().isSquareAvailable(nextPosition.getX(), nextPosition.getY())) {
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
    }

    public boolean isRolling() {
        return (this.rollingPositions != null && this.rollingPositions.size() > 0);
    }

    public List<Position3D> getRollingPositions() {
        return this.rollingPositions;
    }

    public void setRollingPositions(List<Position3D> positions) {
        this.rollingPositions = positions;
    }
}
