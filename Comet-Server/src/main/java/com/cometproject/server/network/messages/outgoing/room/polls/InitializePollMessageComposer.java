package com.cometproject.server.network.messages.outgoing.room.polls;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class InitializePollMessageComposer extends MessageComposer {

    private final int pollId;
    private final String headline;
    private final String thanksMessage;

    public InitializePollMessageComposer(int pollId, String headline, String thanksMessage) {
        this.pollId = pollId;
        this.headline = headline;
        this.thanksMessage = thanksMessage;
    }

    @Override
    public short getId() {
        return Composers.InitializePollMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.pollId);

        msg.writeString(this.headline);
        msg.writeString(this.headline);
        msg.writeString(this.headline);
        msg.writeString(this.thanksMessage);
    }
}
