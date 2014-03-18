package com.cometproject.server.game.rooms.entities.types.ai;

import com.cometproject.server.game.rooms.entities.types.PlayerEntity;

public interface BotAI {
    public boolean onTalk(PlayerEntity entity, String message); // return value indicates whether the message should be broadcasted to room or not.
    public void onPlayerEntityInRange(PlayerEntity entity); // triggered once an entity is within a 5x5 range of the bot
    public void onProcess(); // run every time the bot is processed
}
