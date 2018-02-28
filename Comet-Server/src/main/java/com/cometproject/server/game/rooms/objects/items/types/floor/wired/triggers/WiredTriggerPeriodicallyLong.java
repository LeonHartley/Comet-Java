package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerPeriodicallyLong extends WiredTriggerPeriodically {
    private static final int PARAM_TICK_LENGTH = 0;

    public WiredTriggerPeriodicallyLong(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public int getTickCount() {
        return (this.getWiredData().getParams().get(PARAM_TICK_LENGTH)) * 10;
    }

    @Override
    public int getInterface() {
        return 12;
    }

}
