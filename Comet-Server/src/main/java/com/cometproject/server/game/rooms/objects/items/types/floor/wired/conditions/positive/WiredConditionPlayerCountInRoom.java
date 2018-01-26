package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionPlayerCountInRoom extends WiredConditionItem {
    private static final int PARAM_AT_LEAST = 0;
    private static final int PARAM_NO_MORE_THAN = 1;

    public WiredConditionPlayerCountInRoom(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public int getInterface() {
        return 5;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (this.getWiredData().getParams().size() != 2) return true;

        final int playerCount = this.getRoom().getEntities().getPlayerEntities().size();

        if (playerCount >= this.getWiredData().getParams().get(PARAM_AT_LEAST) && playerCount <= this.getWiredData().getParams().get(PARAM_NO_MORE_THAN)) {
            // true if is not negative, false if is negative
            return !this.isNegative;
        }

        // false if is not negative, true if is negative.
        return this.isNegative;
    }
}
