package com.cometproject.server.network.ws.messages.room;

import com.cometproject.server.network.ws.messages.WsMessage;
import com.cometproject.server.network.ws.messages.WsMessageType;

public class RoomVoteCountMessage extends WsMessage {
    private final int left;
    private final int right;

    public RoomVoteCountMessage(final int left, final int right) {
        super(WsMessageType.ROOM_VOTE_COUNT);

        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }
}
