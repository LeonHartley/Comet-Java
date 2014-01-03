package com.cometsrv.game.items.interactions;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.RoomItem;

public class InteractionQueueItem {
    private boolean needsUpdate;
    private RoomItem item;
    private InteractionAction actionToPerform;
    private Avatar avatar;
    private int updateState;
    private int updateCycles;

    public InteractionQueueItem(boolean needsUpdate, RoomItem item, InteractionAction action, Avatar avatar, int updateState) {
        this.needsUpdate = needsUpdate;
        this.item = item;
        this.actionToPerform = action;
        this.avatar = avatar;
        this.updateState = updateState;
        this.updateCycles = 0;
    }

    public InteractionQueueItem(boolean needsUpdate, RoomItem item, InteractionAction action, Avatar avatar, int updateState, int updateCycles) {
        this.needsUpdate = needsUpdate;
        this.item = item;
        this.actionToPerform = action;
        this.avatar = avatar;
        this.updateState = updateState;
        this.updateCycles = updateCycles;
    }

    public boolean needsCycling() {
        if (this.updateCycles < 1) { return false; }

        this.updateCycles--;

        if (this.updateCycles < 0) {
            this.updateCycles = 0;
        }

        return this.updateCycles > 0;
    }

    public boolean getNeedsUpdate() { return this.needsUpdate; }

    public RoomItem getItem() {
        return this.item;
    }

    public InteractionAction getAction() {
        return this.actionToPerform;
    }

    public Avatar getAvatar() { return this.avatar; }

    public int getUpdateState() { return this.updateState; }

    public int getUpdateCycles() { return this.updateCycles; }
}
