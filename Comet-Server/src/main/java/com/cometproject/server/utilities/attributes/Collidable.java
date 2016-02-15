package com.cometproject.server.utilities.attributes;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;


public interface Collidable {
    public RoomEntity getCollision();

    public void setCollision(RoomEntity entity);

    public void nullifyCollision();
}
