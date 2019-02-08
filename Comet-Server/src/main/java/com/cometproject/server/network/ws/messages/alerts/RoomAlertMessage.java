package com.cometproject.server.network.ws.messages.alerts;

import com.cometproject.server.network.ws.messages.WsMessage;
import com.cometproject.server.network.ws.messages.WsMessageType;

public class RoomAlertMessage extends WsMessage {

    private final String message;
    private final String username;
    private final String figure;

    public RoomAlertMessage(String message, String username, String figure) {
        super(WsMessageType.ROOM_ALERT);
        this.message = message;
        this.username = username;
        this.figure = figure;
    }
}
