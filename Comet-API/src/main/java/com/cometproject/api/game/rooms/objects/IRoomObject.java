package com.cometproject.api.game.rooms.objects;

import com.cometproject.api.game.rooms.IRoom;
import com.cometproject.api.game.rooms.util.IPosition;

public interface IRoomObject {
    IPosition getPosition();

    boolean isAtDoor();

    IRoom getRoom();
}
