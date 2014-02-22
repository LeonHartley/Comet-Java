package com.cometproject.server.game.items.interactions;

import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;

public class InteractionQueueItem {
    private boolean needsUpdate;
    private RoomItem item;
    private InteractionAction actionToPerform;
    private PlayerEntity entity;
    private int updateState;
    private int updateCycles;

    public InteractionQueueItem(boolean needsUpdate, RoomItem item, InteractionAction action, PlayerEntity avatar, int updateState) {
        this.needsUpdate = needsUpdate;
        this.item = item;
        this.actionToPerform = action;
        this.entity = avatar;
        this.updateState = updateState;
        this.updateCycles = 0;
    }

    public InteractionQueueItem(boolean needsUpdate, RoomItem item, InteractionAction action, PlayerEntity avatar, int updateState, int updateCycles) {
        this.needsUpdate = needsUpdate;
        this.item = item;
        this.actionToPerform = action;
        this.entity = avatar;
        this.updateState = updateState;
        this.updateCycles = updateCycles;
    }

    public boolean needsCycling() {
        this.updateCycles--;

        if (this.updateCycles < 0) {
            this.updateCycles = 0;
        }

        return (this.updateCycles > 0);
    }

    public RoomItem getItem() {
        return this.item;
    }

    public InteractionAction getAction() {
        return this.actionToPerform;
    }

    public PlayerEntity getEntity() { return this.entity; }

    public int getUpdateState() { return this.updateState; }

    public int getUpdateCycles() { return this.updateCycles; }
}
