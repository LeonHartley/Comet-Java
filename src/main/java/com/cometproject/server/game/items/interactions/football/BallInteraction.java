package com.cometproject.server.game.items.interactions.football;

import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.utilities.DistanceCalculator;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;

import java.util.ArrayList;
import java.util.List;

public class BallInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {

        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        if (!(item instanceof FloorItem)) {
            return false;
        }

        if (((FloorItem) item).isRolling())
            return false;

        FloorItem floorItem = (FloorItem) item;
        Position3D currentPosition = new Position3D(floorItem.getX(), floorItem.getY(), floorItem.getHeight());

        Position3D newPosition;

        if (avatar.getRoom().getMapping().isValidStep(currentPosition, calculatePosition(floorItem.getX(), floorItem.getY(), avatar.getBodyRotation()), false)) {
            newPosition = calculatePosition(floorItem.getX(), floorItem.getY(), avatar.getBodyRotation());
        } else {
            newPosition = calculatePosition(floorItem.getX(), floorItem.getY(), avatar.getBodyRotation(), true);
        }

        newPosition.setZ((float) floorItem.getRoom().getModel().getSquareHeight()[newPosition.getX()][newPosition.getY()]);

        roll(floorItem, currentPosition, newPosition, avatar.getRoom());

        floorItem.setRotation(avatar.getBodyRotation());
        floorItem.setX(newPosition.getX());
        floorItem.setY(newPosition.getY());

        floorItem.setHeight((float) newPosition.getZ());

        floorItem.setExtraData("2");

        floorItem.setNeedsUpdate(true);
        return false;
    }

    public static void roll(FloorItem item, Position3D from, Position3D to, Room room) {
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

    public static final int KICK_POWER = 6;

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar, boolean isWiredTriggered) {
        if (!(item instanceof FloorItem)) {
            return false;
        }

        if (((FloorItem) item).isRolling())
            return false;

        if (!DistanceCalculator.tilesTouching(item.getX(), item.getY(), avatar.getPosition().getX(), avatar.getPosition().getY()))
            return false;

        FloorItem floorItem = (FloorItem) item;

        floorItem.setRotation(avatar.getBodyRotation());

        List<Position3D> positions = new ArrayList<>();
        Position3D currentPosition = new Position3D(floorItem.getX(), floorItem.getY());
        Position3D nextPosition = currentPosition.squareInFront(avatar.getBodyRotation());

        boolean needsReverse = false;

        for (int i = 0; i < KICK_POWER; i++) {
            if (!floorItem.getRoom().getMapping().isValidStep(currentPosition, nextPosition, false) || !floorItem.getRoom().getEntities().isSquareAvailable(nextPosition.getX(), nextPosition.getY())) {
                needsReverse = true;
                nextPosition = nextPosition.squareBehind(item.getRotation());
                continue;
            }

            if (i > 1)
                positions.add(nextPosition);

            if (needsReverse) {
                nextPosition = nextPosition.squareBehind(item.getRotation());
            } else {
                nextPosition = nextPosition.squareInFront(item.getRotation());
            }
        }

        floorItem.setRollingPositions(positions);
        return false;
    }

    @Override
    public boolean onPlace(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(RoomItem item, PlayerEntity avatar, int updateState) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
