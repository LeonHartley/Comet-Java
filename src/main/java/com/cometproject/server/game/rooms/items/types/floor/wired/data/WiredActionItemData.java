package com.cometproject.server.game.rooms.items.types.floor.wired.data;

import com.cometproject.server.game.rooms.items.types.floor.wired.WiredItemSnapshot;

import java.util.List;

public class WiredActionItemData extends WiredItemData {
    private int delay;

    public WiredActionItemData(int selectionType, List<Integer> selectedIds, String text, int[] params, List<WiredItemSnapshot> snapshots, int delay) {
        super(selectionType, selectedIds, text, params, snapshots);
        this.delay = delay;
    }

    public WiredActionItemData(int delay) {
        this.delay = delay;
    }

    public WiredActionItemData() {
        super();
        this.delay = 0;
    }

    public int getDelay() {
        return delay;
    }
}
