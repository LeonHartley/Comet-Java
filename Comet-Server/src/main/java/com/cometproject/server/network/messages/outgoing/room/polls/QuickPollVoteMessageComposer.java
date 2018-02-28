package com.cometproject.server.network.messages.outgoing.room.polls;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class QuickPollVoteMessageComposer extends MessageComposer {

    private final int playerId;
    private final String myVote;

    private final int yesVotesCount;
    private final int noVotesCount;

    public QuickPollVoteMessageComposer(int playerId, String myVote, int yesVotesCount, int noVotesCount) {
        this.playerId = playerId;
        this.myVote = myVote;
        this.yesVotesCount = yesVotesCount;
        this.noVotesCount = noVotesCount;
    }

    @Override
    public short getId() {
        return Composers.QuickPollResultMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.playerId);
        msg.writeString(myVote);
        msg.writeInt(2);
        msg.writeString("1");
        msg.writeInt(this.yesVotesCount);

        msg.writeString("0");
        msg.writeInt(this.noVotesCount);
    }
}
