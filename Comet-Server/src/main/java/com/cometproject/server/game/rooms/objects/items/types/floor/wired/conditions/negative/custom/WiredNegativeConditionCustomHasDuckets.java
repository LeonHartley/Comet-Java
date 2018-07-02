package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.custom.WiredConditionCustomHasDuckets;
import com.cometproject.server.game.rooms.types.Room;


public class WiredNegativeConditionCustomHasDuckets extends WiredConditionCustomHasDuckets {

    public WiredNegativeConditionCustomHasDuckets(RoomItemData itemData, Room room) {
        super(itemData, room);
    }
}
