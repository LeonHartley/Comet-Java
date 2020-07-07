package com.cometproject.api.game.rooms.objects.data;

public class HighscoreEntry {
    private final int score;
    private final String[] players;

    public HighscoreEntry(int score, String[] players) {
        this.score = score;
        this.players = players;
    }

    public int getScore() {
        return score;
    }

    public String[] getPlayers() {
        return players;
    }
}
