package com.cometproject.server.game.rooms.types.components.games;

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
}
