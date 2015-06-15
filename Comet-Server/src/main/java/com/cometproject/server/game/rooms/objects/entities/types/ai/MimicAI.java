package com.cometproject.server.game.rooms.objects.entities.types.ai;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;

public class MimicAI extends AbstractBotAI {
    public MimicAI(GenericEntity entity) {
        super(entity);
    }

    @Override
    public boolean onTalk(PlayerEntity entity, String message) {
        if (message.startsWith(":")) return false;

        if (entity.getPlayerId() == ((BotEntity) this.getEntity()).getData().getOwnerId()) {
            this.getEntity().getRoom().getEntities().broadcastMessage(new TalkMessageComposer(this.getEntity().getId(), message, 0, 2));
        }

        return false;
    }
}
