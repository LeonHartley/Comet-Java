package com.cometproject.server.network.ws.messages.alerts;

import com.cometproject.server.network.ws.messages.WsMessage;
import com.cometproject.server.network.ws.messages.WsMessageType;

public class EventAlertMessage extends WsMessage {
    private final String eventName;
    private final String eventDescription;
    private final String playerName;
    private final String playerFigure;
    private final int roomId;

    public EventAlertMessage(String eventName, String eventDescription, String playerName, String playerFigure, int roomId) {
        super(WsMessageType.EVENT_ALERT);

        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.playerName = playerName;
        this.playerFigure = playerFigure;
        this.roomId = roomId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerFigure() {
        return playerFigure;
    }

    public int getRoomId() {
        return roomId;
    }
}
