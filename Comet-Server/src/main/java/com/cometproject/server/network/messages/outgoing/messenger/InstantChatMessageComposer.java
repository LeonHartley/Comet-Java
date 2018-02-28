package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class InstantChatMessageComposer extends MessageComposer {
    private final String message;
    private final int fromId;

    private String username;
    private String figure;
    private int playerId;

    public InstantChatMessageComposer(final String message, final int fromId) {
        this.message = message;
        this.fromId = fromId;
    }

    public InstantChatMessageComposer(final String message, final int fromId, final String username, final String figure, final int playerId) {
        this.message = message;
        this.fromId = fromId;
        this.username = username;
        this.figure = figure;
        this.playerId = playerId;
    }

    @Override
    public short getId() {
        return Composers.NewConsoleMessageMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(fromId);
        msg.writeString(message);
        msg.writeInt(0);

        if (this.username != null) { // we can assume the rest aren't null
            final String data = username + "/" + figure + "/" + playerId;
            msg.writeString(data);
        }
    }
}
