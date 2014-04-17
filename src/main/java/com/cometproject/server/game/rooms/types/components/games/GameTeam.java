package com.cometproject.server.game.rooms.types.components.games;

import com.cometproject.server.game.rooms.avatars.effects.UserEffectType;

public enum GameTeam {
    NONE(0),
    RED(1),
    GREEN(2),
    BLUE(3),
    YELLOW(4);

    private final int teamId;

    GameTeam(int team) {
        this.teamId = team;
    }

    public int getTeamId() {
        return this.teamId;
    }

    public int getBanzaiEffect() {
        switch (teamId) {
            case 1:
                return UserEffectType.BB_RED.getEffectId();

            case 2:
                return UserEffectType.BB_GREEN.getEffectId();

            case 3:
                return UserEffectType.BB_BLUE.getEffectId();

            case 4:
                return UserEffectType.BB_YELLOW.getEffectId();
        }

        return 0;
    }
}
