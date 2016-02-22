package com.cometproject.api.game.furniture.types;

import com.cometproject.api.game.players.data.components.inventory.IInventoryItemSnapshot;

public interface ISongItem {

    int getSongId();

    IInventoryItemSnapshot getItemSnapshot();
}
