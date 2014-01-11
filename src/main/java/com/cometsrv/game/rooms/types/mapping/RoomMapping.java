package com.cometsrv.game.rooms.types.mapping;

import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.RoomModel;

public abstract class RoomMapping {

    private final Room room;
    private final RoomModel model;

    private Position3D[][] redirectGrid;

    private RoomTileStatus[][] statusGrid;

    public RoomMapping(Room roomInstance, RoomModel roomModel) {
        this.room = roomInstance;
        this.model = roomModel;
    }

    public boolean isValidRotation(int rotation) {
        return (rotation == 0 || rotation == 2 || rotation == 4 || rotation == 6);
    }

    public double getStepHeight(Position3D position) {
        return 1.0;
    }

    public boolean canStepUpwards(double height0, double height1) {
        double stepHeight = (height0 - height1);

        if (stepHeight > 1.5) {
            return false;
        }

        return true;
    }

    public Position3D getRedirectedPosition(Position3D position) {
        return (this.redirectGrid[position.getX()][position.getY()] != null ? this.redirectGrid[position.getX()][position.getY()] : position);
    }

    public boolean positionHasUser(Position3D position3D) {
        return this.room.getEntities().getEntitiesAt(position3D.getX(), position3D.getY()).size() > 0;
    }

    public boolean isValidPosition(Position3D position) {
        return (position.getX() >= 0 && position.getY() >= 0 && position.getX() < this.model.getSizeX() && position.getY() < this.model.getSizeY());
    }

    public boolean isValidStep(Position3D from, Position3D to, boolean lastStep) {
        // Check we are within the bounds of the room
        if (!isValidPosition(to)) {
            return false;
        }

        // Is the step a door and is it the last step? (if its not then we don't want to walk into the door to get around furniture)
        if (to.getX() == this.model.getDoorX() && to.getY() == this.model.getDoorY() && !lastStep) {
            return false;
        }

        // Is the next step too high for us to walk to?
        if (!canStepUpwards(getStepHeight(to), getStepHeight(from))) {
            return false;
        }
    }

    public abstract void regenerateMap();
}
