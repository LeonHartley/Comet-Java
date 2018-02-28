package com.cometproject.server.network.messages.outgoing.room.freeze;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class UpdateFreezeLivesMessageComposer extends MessageComposer {
    private final int avatar;
    private final int lives;

    public UpdateFreezeLivesMessageComposer(int avatar, int lives) {
        this.avatar = avatar;
        this.lives = lives;
    }

    @Override
    public short getId() {
        return Composers.UpdateFreezeLivesMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.avatar);
        msg.writeInt(this.lives);
    }
}
