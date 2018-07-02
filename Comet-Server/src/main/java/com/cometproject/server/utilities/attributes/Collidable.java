package com.cometproject.server.utilities.attributes;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;


public interface Collidable {
    RoomEntity getCollision();

    void setCollision(RoomEntity entity);

    void nullifyCollision();
}
