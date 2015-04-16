package com.cometproject.api.game.rooms;

public interface RoomCategory {
    int getId();

    String getTitle();

    int getRank();

    boolean canTrade();
}
