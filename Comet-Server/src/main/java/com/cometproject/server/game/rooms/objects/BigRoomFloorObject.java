package com.cometproject.server.game.rooms.objects;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;

public abstract class BigRoomFloorObject extends RoomObject {
    /**
     * The unique identifier of this object
     */
    private long id;

    /**
     * The virtual identifier of this object
     */
    private int virtualId;

    /**
     * Create the room object instance
     *
     * @param position The position in the room where this object is
     * @param room     The room where this object is
     */
    public BigRoomFloorObject(long id, Position position, Room room) {
        super(position, room);

        this.id = id;
        this.virtualId = ItemManager.getInstance().getItemVirtualId(id);
    }

    /**
     * Get the ID of this object
     *
     * @return The ID of this object
     */
    public long getId() {
        return id;
    }

    /**
     * Get the virtual ID of this object
     * @return The virtual ID of this object
     */
    public int getVirtualId() {
        return virtualId;
    }
}
