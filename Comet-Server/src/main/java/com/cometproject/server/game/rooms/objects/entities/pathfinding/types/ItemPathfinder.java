package com.cometproject.server.game.rooms.objects.entities.pathfinding.types;

import com.cometproject.server.game.rooms.objects.RoomObject;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.Pathfinder;
import com.cometproject.server.game.rooms.objects.items.types.floor.RollableFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.WiredActionChase;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.mapping.RoomEntityMovementNode;
import com.cometproject.server.game.rooms.types.mapping.Tile;
import com.cometproject.server.game.rooms.types.tiles.RoomTileState;

public class ItemPathfinder extends Pathfinder {
    private static ItemPathfinder pathfinderInstance;

    public static ItemPathfinder getInstance() {
        if (pathfinderInstance == null) {
            pathfinderInstance = new ItemPathfinder();
        }

        return pathfinderInstance;
    }

    public boolean isValidStep(RoomObject roomObject, Position from, Position to, boolean lastStep) {
        if (from.getX() == to.getX() && from.getY() == to.getY()) {
            return true;
        }

        if (!(to.getX() < roomObject.getRoom().getModel().getSquareState().length)) {
            return false;
        }

        if (!roomObject.getRoom().getMapping().isValidPosition(to) || (roomObject.getRoom().getModel().getSquareState()[to.getX()][to.getY()] == RoomTileState.INVALID)) {
            return false;
        }

        final int rotation = Position.calculateRotation(from, to);

        if (rotation == 1 || rotation == 3 || rotation == 5 || rotation == 7) {
            Tile left = null;
            Tile right = null;

            switch (rotation) {
                case 1:
                    left = roomObject.getRoom().getMapping().getTile(from.squareInFront(rotation + 1));
                    right = roomObject.getRoom().getMapping().getTile(to.squareBehind(rotation + 1));
                    break;

                case 3:
                    left = roomObject.getRoom().getMapping().getTile(to.squareBehind(rotation + 1));
                    right = roomObject.getRoom().getMapping().getTile(to.squareBehind(rotation - 1));
                    break;

                case 5:
                    left = roomObject.getRoom().getMapping().getTile(from.squareInFront(rotation - 1));
                    right = roomObject.getRoom().getMapping().getTile(to.squareBehind(rotation - 1));
                    break;

                case 7:
                    left = roomObject.getRoom().getMapping().getTile(to.squareBehind(0));
                    right = roomObject.getRoom().getMapping().getTile(from.squareInFront(rotation - 1));
                    break;
            }

            if (left != null && right != null) {
                if (left.getMovementNode() != RoomEntityMovementNode.OPEN && right.getMovementNode() != RoomEntityMovementNode.OPEN)
                    return false;
            }
        }

        Tile tile = roomObject.getRoom().getMapping().getTile(to.getX(), to.getY());

        if (tile == null) {
            return false;
        }

        if (roomObject instanceof WiredActionChase) {
            int target = ((WiredActionChase) roomObject).getTargetId();

            if (target != -1) {
                for (GenericEntity entity : tile.getEntities()) {
                    if (entity.getId() != target) {
                        return false;
                    }
                }
            }
        }

        if (roomObject instanceof RollableFloorItem) {
            for (GenericEntity entity : tile.getEntities()) {
                return false;
            }
        }

        if (tile.getMovementNode() == RoomEntityMovementNode.CLOSED || (tile.getMovementNode() == RoomEntityMovementNode.END_OF_ROUTE && !lastStep)) {
            return false;
        }

        final double fromHeight = roomObject.getRoom().getMapping().getStepHeight(from);
        final double toHeight = roomObject.getRoom().getMapping().getStepHeight(to);

        if (fromHeight < toHeight && (toHeight - fromHeight) > 1.0) return false;

        return true;
    }
}
