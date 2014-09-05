package com.cometproject.server.game.rooms.types.mapping;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.models.RoomModel;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.tiles.RoomTileState;

import java.util.ArrayList;
import java.util.List;

public class RoomMapping {
    private Room room;

    private TileInstance[][] tiles;

    public RoomMapping(Room roomInstance) {
        this.room = roomInstance;
    }

    public void init() {
        int sizeX = this.getModel().getSizeX();
        int sizeY = this.getModel().getSizeY();

        this.tiles = new TileInstance[sizeX][sizeY];

        for (int x = 0; x < sizeX; x++) {
            TileInstance[] xArray = new TileInstance[sizeY];

            for (int y = 0; y < sizeY; y++) {
                TileInstance instance = new TileInstance(this, new Position3D(x, y, 0d));
                instance.reload();

                xArray[y] = instance;
            }

            this.tiles[x] = xArray;
        }
    }

    public void updateTile(int x, int y) {
        if (this.tiles.length > x) {
            if (tiles[x].length > y)
                this.tiles[x][y].reload();
        }
    }

    public TileInstance getTile(int x, int y) {
        if(x > this.tiles.length || y > this.tiles[x].length) return null;

        return this.tiles[x][y];
    }

    public boolean positionHasUser(Position3D position) {
        return this.room.getEntities().getEntitiesAt(position.getX(), position.getY()).size() > 0;
    }

    public boolean canStepUpwards(double height0, double height1) {
        return (height0 - height1) <= 1.5;
    }

    public boolean isValidStep(Position3D from, Position3D to, boolean lastStep) {
        return isValidStep(from, to, lastStep, false);
    }

    public boolean isValidStep(Position3D from, Position3D to, boolean lastStep, boolean isFloorItem) {
        if (from.getX() == to.getX() && from.getY() == to.getY()) {
            return true;
        }

        if (!(to.getX() < this.getModel().getSquareState().length)) {
            return false;
        }

        if (!isValidPosition(to) || (this.getModel().getSquareState()[to.getX()][to.getY()] == RoomTileState.INVALID)) {
            return false;
        }

        boolean isAtDoor = this.getModel().getDoorX() == from.getX() && this.getModel().getDoorY() == from.getY();

        if (((!room.getData().getAllowWalkthrough() || isFloorItem) && positionHasUser(to)) && !isAtDoor) {
            return false;

        } else if ((room.getData().getAllowWalkthrough()) && lastStep && positionHasUser(to) && !isAtDoor) {
            return false;
        }

        TileInstance tile = tiles[to.getX()][to.getY()];

        if (tile == null) {
            return false;
        }

        if (tile.getMovementNode() == RoomEntityMovementNode.CLOSED || (tile.getMovementNode() == RoomEntityMovementNode.END_OF_ROUTE && !lastStep)) {
            return false;
        }

        return true;
    }

    public double getStepHeight(Position3D position) {
        TileInstance instance = this.tiles[position.getX()][position.getY()];

        if (!isValidPosition(instance.getPosition())) {
            return 0.0;
        }

        RoomTileStatusType tileStatus = instance.getStatus();
        double height = instance.getStackHeight();

        if (tileStatus == null) {
            return 0.0;
        }

        return height;
    }

    public boolean isValidPosition(Position3D position) {
        return ((position.getX() >= 0) && (position.getY() >= 0) && (position.getX() < this.getModel().getSizeX()) && (position.getY() < this.getModel().getSizeY()));
    }

    public final Room getRoom() {
        return this.room;
    }

    public RoomModel getModel() {
        return this.room.getModel();
    }

    @Override
    public String toString() {
        String mapString = "";

        for(int y = 0; y < this.tiles.length; y++) {
            for(int x = 0; x < this.tiles[y].length; x++) {
                if(this.tiles[y][x].getMovementNode() == RoomEntityMovementNode.CLOSED) {
                    mapString += " ";
                } else {
                    mapString += "X";
                }
            }

            mapString += "\n";
        }

        return mapString;
    }
}
