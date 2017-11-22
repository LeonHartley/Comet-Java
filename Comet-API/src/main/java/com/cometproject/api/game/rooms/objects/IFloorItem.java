package com.cometproject.api.game.rooms.objects;

import com.cometproject.api.game.utilities.Position;

public interface IFloorItem {
    long getId();

    String getDataObject();

    Position getPosition();

    int getRotation();

}
