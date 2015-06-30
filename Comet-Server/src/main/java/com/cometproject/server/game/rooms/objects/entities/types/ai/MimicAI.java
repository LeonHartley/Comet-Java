package com.cometproject.server.game.rooms.objects.entities.types.ai;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.room.avatar.DanceMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.utilities.RandomInteger;

import java.util.Random;

public class MimicAI extends AbstractBotAI {
    public MimicAI(GenericEntity entity) {
        super(entity);
    }

    @Override
    public boolean onTalk(PlayerEntity entity, String message) {
        if (message.startsWith(":")) return false;

        if(message.toLowerCase().contains("minions leave")) {
            this.getEntity().leaveRoom(false, false, false);

            return false;
        } else if(message.toLowerCase().contains("minions dance")) {
            int danceId = RandomInteger.getRandom(1, 4);

            this.getEntity().setDanceId(danceId);
            this.getEntity().getRoom().getEntities().broadcastMessage(new DanceMessageComposer(this.getEntity().getId(), danceId));

            return false;
        } else if(message.toLowerCase().contains("minions stop dancing")) {
            this.getEntity().setDanceId(0);
            this.getEntity().getRoom().getEntities().broadcastMessage(new DanceMessageComposer(this.getEntity().getId(), 0));

            return false;
        } else if(message.toLowerCase().contains("minions sit")) {
            this.getEntity().addStatus(RoomEntityStatus.SIT, "0.5");
            this.getEntity().markNeedsUpdate();

            ((BotEntity) this.getEntity()).getData().setMode("relaxed");

            return false;
        } else if(message.toLowerCase().contains("minions stand")) {
            this.getEntity().removeStatus(RoomEntityStatus.SIT);
            this.getEntity().markNeedsUpdate();

            ((BotEntity) this.getEntity()).getData().setMode("default");

            return false;
        } else if (entity.getPlayerId() == ((BotEntity) this.getEntity()).getData().getOwnerId()) {
            this.getEntity().getRoom().getEntities().broadcastMessage(new TalkMessageComposer(this.getEntity().getId(), message, 0, 2));
        }

        return false;
    }

    @Override
    public boolean onPlayerLeave(PlayerEntity playerEntity) {
        if (playerEntity.getPlayerId() == ((BotEntity) this.getEntity()).getData().getOwnerId()) {
            this.getEntity().leaveRoom(false, false, false);
        }

        return false;
    }
}
