package com.cometproject.server.network.ws.messages.room;

import com.cometproject.server.network.ws.messages.WsMessage;
import com.cometproject.server.network.ws.messages.WsMessageType;
import com.cometproject.server.network.ws.messages.room.data.EventData;

public class RoomVoteMessage extends WsMessage {
    private final String playerName;
    private final String playerFigure;
    private final EventData[] events;
    private final int seconds;

    public RoomVoteMessage(String playerName, String playerFigure, EventData[] events, int seconds) {
        super(WsMessageType.ROOM_VOTE);

        this.playerName = playerName;
        this.playerFigure = playerFigure;
        this.events = events;
        this.seconds = seconds;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerFigure() {
        return playerFigure;
    }

    public EventData[] getEvents() {
        return events;
    }

    public int getSeconds() {
        return seconds;
    }
}

