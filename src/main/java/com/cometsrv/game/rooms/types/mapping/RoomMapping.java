package com.cometsrv.game.rooms.types.mapping;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.RoomModel;
import com.cometsrv.game.rooms.types.tiles.RoomTileState;

public class RoomMapping {
    private final Room room;
    private final RoomModel model;

    private TileInstance[][] tiles;

    private boolean guestsCanPlaceStickies = false;

    public RoomMapping(Room roomInstance, RoomModel roomModel) {
        this.room = roomInstance;
        this.model = roomModel;
    }

    public void init() {
        int sizeX = this.model.getSizeX();
        int sizeY = this.model.getSizeY();

        this.tiles = new TileInstance[sizeX][sizeY];

        for(int x = 0; x < sizeX; x++) {
            for(int y = 0; y < sizeY; y++) {
                TileInstance instance = new TileInstance(this, new Position3D(x, y, 0d));
                instance.reload();

                this.tiles[x][y] = instance;

                System.out.println("TileX: " + x + ", TileY: " + y);
            }
        }
    }

    public void updateTile(int x, int y) {
        this.tiles[x][y].reload();
    }

    public boolean positionHasUser(Position3D position) {
        return this.room.getEntities().getEntitiesAt(position.getX(), position.getY()).size() > 0;
    }

    public boolean canStepUpwards(double height0, double height1) {
        return (height0 - height1) <= 1.5;
    }

    public boolean isValidStep(Position3D from, Position3D to, boolean lastStep) {
        if(!isValidPosition(to) || (this.model.getSquareState()[to.getX()][to.getY()] == RoomTileState.INVALID) || positionHasUser(to)) {
            return false;
        }

        TileInstance tile = tiles[to.getX()][to.getY()];

        if(tile.getMovementNode() == RoomEntityMovementNode.CLOSED
                || tile.getMovementNode() == RoomEntityMovementNode.END_OF_ROUTE && !lastStep) {
            return false;
        }

        if(to.getX() == this.model.getDoorX() && to.getY() == this.model.getDoorY() && !lastStep) {
            return false;
        }

        if (!canStepUpwards(getStepHeight(to), getStepHeight(from))) {
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
        boolean isOutOfBounds = false;
        boolean isTileNull = false;

        if(this.model.getSizeX() < position.getX() || this.model.getSizeY() < position.getY()) {
            isOutOfBounds = true;
        }

        if(this.tiles[position.getX()][position.getY()] == null) {
            isTileNull = true;
        }

        return !isOutOfBounds || !isTileNull;
    }

    public final Room getRoom() {
        return this.room;
    }

    public final RoomModel getModel() {
        return this.model;
    }

    public boolean canGuestsPlaceStickies() {
        return this.guestsCanPlaceStickies;
    }

    public void setGuestsPlaceStickies(boolean guestsCanPlaceStickies) {
        this.guestsCanPlaceStickies = guestsCanPlaceStickies;
    }
}
