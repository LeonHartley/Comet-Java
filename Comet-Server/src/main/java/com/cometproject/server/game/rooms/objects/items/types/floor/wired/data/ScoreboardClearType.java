package com.cometproject.server.game.rooms.objects.items.types.floor.wired.data;

public enum ScoreboardClearType {
    ALL_TIME(0),
    DAILY(1),
    WEEKLY(2),
    MONTHLY(3);

    private int clearType;

    ScoreboardClearType(int clearType) {
        this.clearType = clearType;
    }

    public int getClearTypeId() {
        return clearType;
    }

    public static ScoreboardClearType getByFurniType(int type) {
        switch (type) {
            case 1:
                return ALL_TIME;
            case 2:
                return DAILY;
            case 3:
                return WEEKLY;
            case 4:
                return MONTHLY;
        }

        return ALL_TIME;
    }
}
