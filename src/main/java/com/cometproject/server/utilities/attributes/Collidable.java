package com.cometproject.server.utilities.attributes;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;


public interface Collidable {
    public GenericEntity getCollision();

    public void setCollision(GenericEntity entity);

    public void nullifyCollision();
}
