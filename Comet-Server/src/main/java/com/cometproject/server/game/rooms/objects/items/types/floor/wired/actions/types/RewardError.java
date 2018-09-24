package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.types;

public enum RewardError {
    NONE(-1),
    OUT_OF_STOCK(0),
    ALREADY_GIVEN(1),
    ONE_DAY(2),
    ONE_HOUR(3),
    UNLUCKY(4),
    ONE_MINUTE(8);


    private int type;

    RewardError(int type) {
        this.type = type;
    }

    public int getInteger() {
        return this.type;
    }
}
