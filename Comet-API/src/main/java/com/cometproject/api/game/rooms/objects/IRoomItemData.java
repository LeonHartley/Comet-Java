package com.cometproject.api.game.rooms.objects;

import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.game.utilities.Position;

public interface IRoomItemData {

    long getId();

    int getItemId();

    int getOwnerId();

    String getOwnerName();

    Position getPosition();

    String getWallPosition();

    int getRotation();

    void setRotation(int rotation);

    String getData();

    void setData(String data);

    void setData(int data);

    void setPosition(Position position);

    void setWallPosition(String wallPosition);

    LimitedEditionItem getLimitedEdition();
}
