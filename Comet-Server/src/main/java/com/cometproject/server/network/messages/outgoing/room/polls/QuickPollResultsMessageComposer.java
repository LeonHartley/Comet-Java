package com.cometproject.server.network.messages.outgoing.room.polls;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class QuickPollResultsMessageComposer extends MessageComposer {

    private final int yesVotesCount;
    private final int noVotesCount;

    public QuickPollResultsMessageComposer(int yesVotesCount, int noVotesCount) {
        this.yesVotesCount = yesVotesCount;
        this.noVotesCount = noVotesCount;
    }

    @Override
    public short getId() {
        return Composers.QuickPollResultsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(-1);
        msg.writeInt(2);
        msg.writeString("1");
        msg.writeInt(this.yesVotesCount);

        msg.writeString("0");
        msg.writeInt(this.noVotesCount);
    }
}
