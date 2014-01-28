package com.cometsrv.game.rooms.types.mapping;

import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.RoomModel;
import com.cometsrv.game.rooms.types.tiles.RoomTileState;
import com.cometsrv.utilities.TimeSpan;

import java.util.AbstractQueue;
import java.util.List;

public class RoomMapping {
    private final Room room;
    private final RoomModel model;

    private TileInstance[][] tiles;

    public RoomMapping(Room roomInstance, RoomModel roomModel) {
        this.room = roomInstance;
        this.model = roomModel;
    }

    public void init() {

    }
}
