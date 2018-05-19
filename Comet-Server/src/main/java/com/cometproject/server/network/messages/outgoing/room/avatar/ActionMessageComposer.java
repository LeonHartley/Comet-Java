package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class ActionMessageComposer extends MessageComposer {
    private final int playerId;
    private final int actionId;

    public ActionMessageComposer(final int playerId, final int actionId) {
        this.playerId = playerId;
        this.actionId = actionId;
    }

    @Override
    public short getId() {
        return Composers.ActionMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(playerId);
        msg.writeInt(actionId);
    }
}