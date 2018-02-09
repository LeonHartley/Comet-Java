package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;

public class WiredConditionTimeLessThan extends WiredConditionItem {
    private static final int PARAM_TICKS = 0;

    public WiredConditionTimeLessThan(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public int getInterface() {
        return 3;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        final int ticks = this.getWiredData().getParams().get(PARAM_TICKS);
        return this.getRoom().getWiredTimer() <= ticks;
    }
}

