package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.WiredConditionMatchSnapshot;
import com.cometproject.server.game.rooms.types.Room;


public class WiredNegativeConditionMatchSnapshot extends WiredConditionMatchSnapshot {
    public WiredNegativeConditionMatchSnapshot(RoomItemData itemData, Room room) {
        super(itemData, room);
    }
}
