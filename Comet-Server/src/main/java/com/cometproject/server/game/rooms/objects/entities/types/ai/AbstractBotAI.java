package com.cometproject.server.game.rooms.objects.entities.types.ai;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;

public abstract class AbstractBotAI implements BotAI {
    private GenericEntity entity;

    public AbstractBotAI(GenericEntity entity) {
        this.entity = entity;
    }

    public boolean onTalk(PlayerEntity entity, String message) {
        return false;
    }

    public GenericEntity getEntity() {
        return entity;
    }
}
