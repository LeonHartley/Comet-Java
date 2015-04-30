package com.cometproject.server.game.items.music;

import com.cometproject.server.game.players.components.types.inventory.InventoryItemSnapshot;

public class SongItem {

    private InventoryItemSnapshot itemSnapshot;
    private int songId;

    public SongItem(InventoryItemSnapshot itemSnapshot, int songId) {
        this.itemSnapshot = itemSnapshot;
        this.songId = songId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public InventoryItemSnapshot getItemSnapshot() {
        return itemSnapshot;
    }

    public void setItemSnapshot(InventoryItemSnapshot itemSnapshot) {
        this.itemSnapshot = itemSnapshot;
    }
}
