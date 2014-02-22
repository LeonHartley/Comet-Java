package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class ApplyEffectMessageComposer {
    public static Composer compose(int avatarId, int effectId) {
        Composer msg = new Composer(Composers.ApplyEffectMessageComposer);

        msg.writeInt(avatarId);
        msg.writeInt(effectId);
        msg.writeInt(0);

        return msg;
    }
}
