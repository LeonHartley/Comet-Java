package com.cometproject.server.game.rooms.objects.entities.types.ai;

import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;


public interface BotAI {
    boolean onTalk(PlayerEntity entity, String message); // return value indicates whether the message should be broadcasted to room or not.

    boolean onPlayerLeave(PlayerEntity playerEntity);
}
