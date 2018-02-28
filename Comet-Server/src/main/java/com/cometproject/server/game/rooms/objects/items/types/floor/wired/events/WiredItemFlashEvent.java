package com.cometproject.server.game.rooms.objects.items.types.floor.wired.events;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredFloorItem;

public class WiredItemFlashEvent extends WiredItemEvent {

    public WiredItemFlashEvent() {
        super(null, null);

        this.setTotalTicks(2);
    }

    @Override
    public void onCompletion(final RoomItemFloor floorItem) {
        if (floorItem instanceof WiredFloorItem) {
            ((WiredFloorItem) floorItem).switchState();
        }
    }

    @Override
    public boolean isInteractiveEvent() {
        return false;
    }
}
