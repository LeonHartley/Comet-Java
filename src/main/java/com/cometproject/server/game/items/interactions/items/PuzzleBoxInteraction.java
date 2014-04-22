package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;

public class PuzzleBoxInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar, boolean isWiredTriggered) {
        if (!isWiredTriggered) {
            if (!item.touching(avatar)) {
                avatar.moveTo(item.getX(), item.getY());
                return false;
            }
        }

        if (!(item instanceof FloorItem)) {
            return false;
        }

        if (((FloorItem) item).isRolling())
            return false;

        FloorItem floorItem = (FloorItem) item;
        Position3D currentPosition = new Position3D(floorItem.getX(), floorItem.getY(), floorItem.getHeight());

        Position3D newPosition = null;

        if (avatar.getRoom().getMapping().isValidStep(currentPosition, calculatePosition(floorItem.getX(), floorItem.getY(), avatar.getBodyRotation()), false)) {
            newPosition = calculatePosition(floorItem.getX(), floorItem.getY(), avatar.getBodyRotation());
        }// else {
        //    newPosition = calculatePosition(floorItem.getX(), floorItem.getY(), avatar.getBodyRotation(), true);
        //}

        if (newPosition == null)
            return false;

        newPosition.setZ((float) floorItem.getRoom().getModel().getSquareHeight()[newPosition.getX()][newPosition.getY()]);

        roll(floorItem, currentPosition, newPosition, avatar.getRoom());

        floorItem.setRotation(avatar.getBodyRotation());
        floorItem.setX(newPosition.getX());
        floorItem.setY(newPosition.getY());

        floorItem.setHeight((float) newPosition.getZ());

        floorItem.getRoom().getMapping().getTile(currentPosition.getX(), currentPosition.getY()).reload();
        floorItem.getRoom().getMapping().getTile(newPosition.getX(), newPosition.getY()).reload();

        avatar.moveTo(currentPosition.getX(), currentPosition.getY());
        avatar.setBodyRotation(Position3D.calculateRotation(currentPosition.getX(), currentPosition.getY(), newPosition.getX(), newPosition.getY(), false));
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
            case 1:
                if (!isReversed)
                    y--;
                else
                    y++;
                break;

            case 2:
            case 3:
                if (!isReversed)
                    x++;
                else
                    x--;
                break;


            case 4:
            case 5:
                if (!isReversed)
                    y++;
                else
                    y--;
                break;

            case 6:
            case 7:
                if (!isReversed)
                    x--;
                else
                    x++;
                break;
        }

        return new Position3D(x, y);
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
