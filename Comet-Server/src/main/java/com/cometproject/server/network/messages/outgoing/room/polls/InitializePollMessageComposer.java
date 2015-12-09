package com.cometproject.server.network.messages.outgoing.room.polls;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class InitializePollMessageComposer extends MessageComposer {

    private final int pollId;

    private final String headline;
    private final String summary;
    private final String unk1;
    private final String unk2;

    public InitializePollMessageComposer(int pollId, String headline, String summary, String unk1, String unk2) {
        this.pollId = pollId;
        this.headline = headline;
        this.summary = summary;
        this.unk1 = unk1;
        this.unk2 = unk2;
    }

    @Override
    public short getId() {
        return Composers.InitializePollMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.pollId);

        msg.writeString(this.headline);
        msg.writeString(this.summary);
        msg.writeString(this.unk1);
        msg.writeString(this.unk2);
    }
}
