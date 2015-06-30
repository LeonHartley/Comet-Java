package com.cometproject.server.game.rooms.objects.entities.types.ai;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;


public class DefaultAI implements BotAI {
    private GenericEntity entity;

    public DefaultAI(GenericEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean onTalk(PlayerEntity entity, String message) {
        return false;
    }

    @Override
    public boolean onPlayerLeave(PlayerEntity playerEntity) {
        return false;
    }
}
