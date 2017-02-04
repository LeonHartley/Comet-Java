package com.cometproject.server.game.guides.types;

public class HelpRequest {
    private final int playerId;

    private final int type;
    private final String message;

    private boolean recommendation = false;

    public HelpRequest(final int playerId, final int type, final String message) {
        this.playerId = playerId;
        this.type = type;
        this.message = message;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
