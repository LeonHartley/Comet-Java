package com.cometproject.api.game.furniture.types;

import com.cometproject.api.game.players.data.components.inventory.PlayerItemSnapshot;

public interface SongItem {

    int getSongId();

    PlayerItemSnapshot getItemSnapshot();
}
