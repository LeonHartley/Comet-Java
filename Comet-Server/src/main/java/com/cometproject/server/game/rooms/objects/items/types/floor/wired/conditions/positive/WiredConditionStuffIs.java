package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;

public class WiredConditionStuffIs extends WiredConditionItem {

    public WiredConditionStuffIs(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public int getInterface() {
        return 8;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (data == null || !(data instanceof RoomItemFloor)) {
            return false;
        }

        final RoomItemFloor floor = ((RoomItemFloor) data);
        boolean result = false;

        if (this.getWiredData().getSelectedIds().contains(floor.getId())) {
            result = true;
        }

        return !this.isNegative == result;
    }
}
