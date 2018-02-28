package com.cometproject.server.network.messages.incoming.polls;

import com.cometproject.server.game.polls.PollManager;
import com.cometproject.server.game.polls.types.Poll;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.polls.PollMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class GetPollMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int pollId = msg.readInt();

        Poll poll = PollManager.getInstance().getPollbyId(pollId);

        if (poll == null) {
            return;
        }

        client.send(new PollMessageComposer(poll));
    }
}