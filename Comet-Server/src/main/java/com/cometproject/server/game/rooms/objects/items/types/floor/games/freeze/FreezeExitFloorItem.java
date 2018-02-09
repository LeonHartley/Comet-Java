package com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class FreezeExitFloorItem extends RoomItemFloor {
    public FreezeExitFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        this.getItemData().setData("0");
    }
}
