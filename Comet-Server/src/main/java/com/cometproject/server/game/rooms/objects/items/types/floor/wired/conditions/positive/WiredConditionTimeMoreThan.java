package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;

public class WiredConditionTimeMoreThan extends WiredConditionItem {
    private static final int PARAM_TICKS = 0;

    public WiredConditionTimeMoreThan(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        this.getWiredData().getParams().putIfAbsent(PARAM_TICKS, 2); // 1s
    }

    @Override
    public int getInterface() {
        return 3;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        final int ticks = this.getWiredData().getParams().get(PARAM_TICKS);
        return this.getRoom().getWiredTimer() >= ticks;
    }
}
