package com.cometproject.server.game.rooms.objects;

import com.cometproject.api.game.rooms.objects.IRoomObject;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.objects.misc.Positionable;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;


public abstract class RoomFloorObject extends RoomObject implements Positionable {
    /**
     * The unique identifier of this object
     */
    private int id;

    /**
     * Create the room object instance
     *
     * @param position The position in the room where this object is
     * @param room     The room where this object is
     */
    public RoomFloorObject(int id, Position position, Room room) {
        super(position, room);

        this.id = id;
    }

    /**
     * Get the ID of this object
     *
     * @return The ID of this object
     */
    public int getId() {
        return id;
    }
}
