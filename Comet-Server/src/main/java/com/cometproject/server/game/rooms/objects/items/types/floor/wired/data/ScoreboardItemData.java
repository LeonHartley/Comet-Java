package com.cometproject.server.game.rooms.objects.items.types.floor.wired.data;

import com.cometproject.server.utilities.comporators.HighscoreComparator;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardItemData {
    private final static HighscoreComparator comparator = new HighscoreComparator();
    private final List<HighscoreEntry> entries;
    private final Map<String, Integer> points;
    private int scoreType;
    private int clearType;

    public ScoreboardItemData(int scoreType, int clearType, List<HighscoreEntry> entries, Map<String, Integer> points) {
        this.scoreType = scoreType;
        this.clearType = clearType;
        this.entries = entries;

        this.points = points;
    }

    public List<HighscoreEntry> getEntries() {
        return this.entries;
    }

    public Map<String, Integer> getPoints() {
        return this.points;
    }

    public void addEntry(List<String> users, int score) {
        synchronized (this.entries) {
            this.entries.add(new HighscoreEntry(users, score));

            Collections.sort(this.entries, comparator);
        }
    }

    public void addPoint(String identifier, int score) {
        if (!this.points.containsKey(identifier)) {
            this.points.put(identifier, score);
            return;
        }

        this.points.replace(identifier, this.points.get(identifier) + score);
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
