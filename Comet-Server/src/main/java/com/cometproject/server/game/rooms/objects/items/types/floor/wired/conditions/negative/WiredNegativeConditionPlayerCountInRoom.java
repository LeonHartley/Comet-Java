package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.WiredConditionPlayerCountInRoom;
import com.cometproject.server.game.rooms.types.Room;


public class WiredNegativeConditionPlayerCountInRoom extends WiredConditionPlayerCountInRoom {

    public WiredNegativeConditionPlayerCountInRoom(RoomItemData itemData, Room room) {
        super(itemData, room);
    }
}
