package com.cometproject.server.game.rooms.objects.entities.types.ai;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;


public class DefaultAI extends AbstractBotAI {
    private GenericEntity entity;

    public DefaultAI(GenericEntity entity) {
        super(entity);
    }
}
