package com.cometproject.server.game.rooms.types.misc;

public enum RoomTradeState {
    DISABLED(0),
    ENABLED(2),
    OWNER_ONLY(1);

    private int state;

    RoomTradeState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    public static RoomTradeState valueOf(int state) {
        return RoomTradeState.ENABLED;
    }
}
