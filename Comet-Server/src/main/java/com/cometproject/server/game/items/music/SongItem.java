package com.cometproject.server.game.items.music;

import com.cometproject.api.game.furniture.types.ISongItem;
import com.cometproject.api.game.players.data.components.inventory.IInventoryItemSnapshot;
import com.cometproject.server.game.players.components.types.inventory.InventoryItemSnapshot;

public class SongItem implements ISongItem {

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

    public IInventoryItemSnapshot getItemSnapshot() {
        return itemSnapshot;
    }
}
