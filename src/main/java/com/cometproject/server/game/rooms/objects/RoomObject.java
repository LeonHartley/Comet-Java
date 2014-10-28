package com.cometproject.server.game.rooms.objects;

import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.TileInstance;

public abstract class RoomObject {
    /**
     * The unique identifier of this object
     */
    private int id;

    /**
     * The room where this object resides
     */
    private Room room;

    /**
     * The position on the grid this object resides
     */
    private Position position;

    /**
     * Create the room object instance
     * @param position The position in the room where this object is
     * @param room The room where this object is
     */
    public RoomObject(int id, Position position, Room room) {
        this.id = id;
        this.position = position;
        this.room = room;
    }

    /**
     * Gets the tile instance from the room mapping
     * @return the tile instance from the room mapping
     */
    public TileInstance getTile() {
        if(this.getPosition() == null) return null;

        return this.getRoom().getMapping().getTile(this.getPosition().getX(), this.getPosition().getY());
    }

    /**
     * Set the position to a new position
     * @param newPosition The position to replace the current one with
     */
    public void setPosition(Position newPosition) {
        if(this.position == null) {
            this.position = newPosition.copy();
        } else {
            this.position.setX(newPosition.getX());
            this.position.setY(newPosition.getY());
            this.position.setZ(newPosition.getZ());
        }
    }

    /**
     * Checks whether or not the object is at the door tile
     * @return Is the object on the door tile?
     */
    public boolean isAtDoor() {
        return this.position.getX() == this.getRoom().getModel().getDoorX() && this.position.getY() == this.getRoom().getModel().getDoorY();
    }

    /**
     * Get the room where this object is
     * @return The room instance
     */
    public Room getRoom() {
        return this.room;
    }

    /**
     * Get the position in which this object is on the grid
     * @return The position instance
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Get the ID of this object
     * @return The ID of this object
     */
    public int getId() {
        return id;
    }
}
