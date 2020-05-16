package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.custom.WiredConditionCustomHasRank;
import com.cometproject.server.game.rooms.types.Room;


public class WiredNegativeConditionCustomHasRank extends WiredConditionCustomHasRank {

    public WiredNegativeConditionCustomHasRank(RoomItemData itemData, Room room) {
        super(itemData, room);
    }
}
