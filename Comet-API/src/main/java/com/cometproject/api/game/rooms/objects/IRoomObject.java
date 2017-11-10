package com.cometproject.api.game.rooms.objects;

import com.cometproject.api.game.rooms.IRoom;
import com.cometproject.api.game.utilities.Position;

public interface IRoomObject {
    Position getPosition();

    boolean isAtDoor();

    IRoom getRoom();
}
