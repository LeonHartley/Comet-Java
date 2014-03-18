package com.cometproject.server.game.rooms.entities.types.ai;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.BotEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;

public class DefaultAI implements BotAI {
    private GenericEntity entity;

    public DefaultAI(GenericEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean onTalk(PlayerEntity entity, String message) {
        // this can be used to handle pet commands as well as bot commands, fyi

        if(message.startsWith("walk to")) {
            int x = Integer.parseInt(message.split("walk to")[1].split(" ")[0]);
            int y = Integer.parseInt(message.split("walk to")[1].split(" ")[1]);

            entity.moveTo(x, y);
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerEntityInRange(PlayerEntity entity) {

    }

    @Override
    public void onProcess() {

    }
}
