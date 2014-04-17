package com.cometproject.server.game.players.components.types;

public enum RelationshipLevel {
    BOBBA,
    SMILE,
    HEART;

    public static RelationshipLevel getLevel(String level) {
        if (level.equals("bobba")) {
            return RelationshipLevel.BOBBA;
        } else if (level.equals("heart")) {
            return RelationshipLevel.HEART;
        }

        return RelationshipLevel.SMILE;
    }

    public static int getInt(RelationshipLevel l) {
        if (l == HEART) {
            return 1;
        } else if (l == SMILE) {
            return 2;
        } else if (l == BOBBA) {
            return 3;
        }

        return 0;
    }
}
