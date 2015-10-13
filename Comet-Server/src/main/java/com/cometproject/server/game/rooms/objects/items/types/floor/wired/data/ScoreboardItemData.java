package com.cometproject.server.game.rooms.objects.items.types.floor.wired.data;

import com.cometproject.server.utilities.comporators.HighscoreComparator;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class ScoreboardItemData {
    private final static HighscoreComparator comparator = new HighscoreComparator();

    private int scoreType;
    private int clearType;

    private final List<HighscoreEntry> entries;

    public ScoreboardItemData(int scoreType, int clearType, List<HighscoreEntry> entries) {
        this.scoreType = scoreType;
        this.clearType = clearType;
        this.entries = entries;
    }

    public List<HighscoreEntry> getEntries() {
        return this.entries;
    }

    public void addEntry(List<String> users, int score) {
        synchronized (this.entries) {
            this.entries.add(new HighscoreEntry(users, score));

            Collections.sort(this.entries, comparator);
        }
    }

    public int getScoreType() {
        return scoreType;
    }

    public void setScoreType(int scoreType) {
        this.scoreType = scoreType;
    }

    public int getClearType() {
        return clearType;
    }

    public void setClearType(int clearType) {
        this.clearType = clearType;
    }

    public class HighscoreEntry {
        private List<String> users;
        private int score;

        public HighscoreEntry(List<String> users, int score) {
            this.users = users;
            this.score = score;
        }

        public List<String> getUsers() {
            return users;
        }

        public void setUsers(List<String> users) {
            this.users = users;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
