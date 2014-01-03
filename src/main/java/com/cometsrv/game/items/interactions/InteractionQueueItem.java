package com.cometsrv.game.items.interactions;

import com.cometsrv.game.rooms.items.FloorItem;

public class InteractionQueueItem {
    private FloorItem floorItem;
    private InteractionAction actionToPerform;

    public InteractionQueueItem(FloorItem item, InteractionAction action) {
        this.floorItem = item;
        this.actionToPerform = action;
    }

    public FloorItem getItem() {
        return this.floorItem;
    }

    public InteractionAction getAction() {
        return this.actionToPerform;
    }
}
