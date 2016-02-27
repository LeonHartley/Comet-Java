package com.cometproject.api.game.rooms.entities;

import com.cometproject.api.game.players.BasePlayer;

public interface PlayerRoomEntity extends RoomEntity {
    BasePlayer getPlayer();
}
