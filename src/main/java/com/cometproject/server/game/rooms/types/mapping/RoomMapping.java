package com.cometproject.server.game.rooms.types.mapping;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomModel;
import com.cometproject.server.game.rooms.types.tiles.RoomTileState;

public class RoomMapping {
    private final Room room;
    private final RoomModel model;

    private TileInstance[][] tiles;

    public RoomMapping(Room roomInstance, RoomModel roomModel) {
        this.room = roomInstance;
        this.model = roomModel;
    }

    public void init() {
        int sizeX = this.model.getSizeX();
        int sizeY = this.model.getSizeY();

        this.tiles = new TileInstance[sizeX][sizeY];

        for(int x = 0; x < sizeX; x++) {
            TileInstance[] xArray = new TileInstance[sizeY];

            for(int y = 0; y < sizeY; y++) {
                TileInstance instance = new TileInstance(this, new Position3D(x, y, 0d));
                instance.reload();

                xArray[y] = instance;
            }

            this.tiles[x] = xArray;
        }
    }

    public void updateTile(int x, int y) {
        this.tiles[x][y].reload();
    }

    public TileInstance getTile(int x, int y) {
        return this.tiles[x][y];
    }

    public boolean positionHasUser(Position3D position) {
        return this.room.getEntities().getEntitiesAt(position.getX(), position.getY()).size() > 0;
    }

    public boolean canStepUpwards(double height0, double height1) {
        return (height0 - height1) <= 1.5;
    }

    public boolean isValidStep(Position3D from, Position3D to, boolean lastStep) {
        if(from.getX() == to.getX() && from.getY() == to.getY()) {
            return true;
        }

        if(!isValidPosition(to) || (this.model.getSquareState()[to.getX()][to.getY()] == RoomTileState.INVALID) || positionHasUser(to)) {
            return false;
        }

        TileInstance tile = tiles[to.getX()][to.getY()];

        if(tile == null) {
            return false;
        }

        if(tile.getMovementNode() == RoomEntityMovementNode.CLOSED || (tile.getMovementNode() == RoomEntityMovementNode.END_OF_ROUTE && !lastStep)) {
            return false;
        }

        //if (!canStepUpwards(getStepHeight(to), getStepHeight(from))) {
        //    return false;
        //}

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
        return ((position.getX() >= 0) && (position.getY() >= 0) && (position.getX() < this.model.getSizeX()) && (position.getY() < this.model.getSizeY()));
    }

    public final Room getRoom() {
        return this.room;
    }

    public final RoomModel getModel() {
        return this.model;
    }
}
