package com.cometproject.server.network.messages.outgoing.room.polls;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class InitializePollMessageComposer extends MessageComposer {

    private final int pollId;
    private final String headline;

    public InitializePollMessageComposer(int pollId, String headline) {
        this.pollId = pollId;
        this.headline = headline;
    }

    @Override
    public short getId() {
        return Composers.InitializePollMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.pollId);

        msg.writeString("");
        msg.writeString("");
        msg.writeString(this.headline);
        msg.writeString("");
    }
}
