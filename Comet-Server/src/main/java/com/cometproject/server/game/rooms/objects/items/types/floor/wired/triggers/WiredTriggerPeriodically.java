package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerPeriodically extends WiredTriggerItem {
    private static final int PARAM_TICK_LENGTH = 0;

    private final WiredItemEvent event;

    public WiredTriggerPeriodically(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        this.getWiredData().getParams().putIfAbsent(PARAM_TICK_LENGTH, 5);

        this.event = new WiredItemEvent(null, null);

        event.setTotalTicks(this.getTickCount());
        this.queueEvent(event);
    }

    @Override
    public boolean suppliesPlayer() {
        return false;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        this.evaluate(null, null);

        // loop
        this.event.setTotalTicks(this.getTickCount());
        this.queueEvent(this.event);
    }

    @Override
    public void onDataChange() {
        this.event.setTotalTicks(this.getTickCount());
    }

    @Override
    public int getInterface() {
        return 6;
    }

    public int getTickCount() {
        int tickLength = this.getWiredData().getParams().get(PARAM_TICK_LENGTH);

        if (tickLength <= 0) {
            tickLength = 2;
        }

        return tickLength;
    }
}
