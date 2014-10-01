package com.cometproject.server.game.rooms.objects;

import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;

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
