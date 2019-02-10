package com.cometproject.server.network.ws.messages.room;

import com.cometproject.server.network.ws.messages.WsMessage;
import com.cometproject.server.network.ws.messages.WsMessageType;

public class RoomVoteEndedMessage extends WsMessage {
    public RoomVoteEndedMessage() {
        super(WsMessageType.ROOM_VOTE_ENDED);
    }
}
