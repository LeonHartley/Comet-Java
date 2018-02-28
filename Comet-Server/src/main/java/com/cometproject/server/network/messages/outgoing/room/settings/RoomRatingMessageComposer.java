package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class RoomRatingMessageComposer extends MessageComposer {
    private final int score;
    private final boolean canRate;

    public RoomRatingMessageComposer(int score, boolean canRate) {
        this.score = score;
        this.canRate = canRate;
    }

    @Override
    public short getId() {
        return Composers.RoomRatingMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.score);
        msg.writeBoolean(this.canRate);
    }
}
