package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.custom.WiredTriggerCustomIdle;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class IdleStatusMessageComposer extends MessageComposer {
    private final int playerId;
    private final boolean isIdle;

    public IdleStatusMessageComposer(final PlayerEntity playerEntity, final boolean isIdle) {
        this.playerId = playerEntity.getId();
        this.isIdle = isIdle;

        WiredTriggerCustomIdle.executeTriggers(playerEntity);
    }

    @Override
    public short getId() {
        return Composers.SleepMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.playerId);
        msg.writeBoolean(this.isIdle);
    }
}
