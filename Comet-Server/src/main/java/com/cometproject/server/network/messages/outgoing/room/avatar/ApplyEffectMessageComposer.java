package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class ApplyEffectMessageComposer extends MessageComposer {

    private final int avatarId;
    private final int effectId;

    public ApplyEffectMessageComposer(final int avatarId, final int effectId) {
        this.avatarId = avatarId;
        this.effectId = effectId;
    }

    @Override
    public short getId() {
        return Composers.ApplyEffectMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(avatarId);
        msg.writeInt(effectId);
        msg.writeInt(0);
    }
}
