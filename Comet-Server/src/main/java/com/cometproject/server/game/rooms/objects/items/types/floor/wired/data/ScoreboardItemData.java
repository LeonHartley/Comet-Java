package com.cometproject.server.game.rooms.objects.items.types.floor.wired.data;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.utilities.comporators.HighscoreComparator;
import com.google.common.collect.Lists;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScoreboardItemData {
    private final static HighscoreComparator comparator = new HighscoreComparator();

    private final Map<String, List<HighscoreEntry>> entries;
    private long lastClearTimestamp;
    private List<HighscoreEntry> currentSortedScores;
    private int currentLowestScore;

    public ScoreboardItemData(long lastClear, CopyOnWriteArrayList<HighscoreEntry> entries) {
        this.lastClearTimestamp = lastClear;
        this.entries = new ConcurrentHashMap<>();

        this.currentLowestScore = 0;
        this.currentSortedScores = null;

        loadEntries(entries);
    }

    private void loadEntries(List<HighscoreEntry> entries) {
        for (HighscoreEntry entry : entries) {
            addEntry(entry, false);
        }
    }

    public List<HighscoreEntry> getTopScores() {
        if (this.currentSortedScores != null) {
            return this.currentSortedScores;
        }

        if (this.entries.size() == 0) {
            return Lists.newArrayList();
        }

        final List<HighscoreEntry> allEntries = this.entries.values().stream()
                .collect(ArrayList::new, List::addAll, List::addAll);

        allEntries.sort(comparator);

        final int lastEntryIndex = Math.min(allEntries.size(), 50);
        final List<HighscoreEntry> sortedTopScores = allEntries.subList(0, lastEntryIndex);
        if (sortedTopScores.size() == 0) {
            return Lists.newArrayList();
        }

        final HighscoreEntry lowestScore = sortedTopScores.get(lastEntryIndex - 1);

        this.currentLowestScore = lowestScore.getScore();
        this.currentSortedScores = sortedTopScores;
        return sortedTopScores;
    }

    public void addEntry(HighscoreEntry entry, boolean updateExisting) {
        addEntry(entry, updateExisting, false);
    }

    public void addEntry(HighscoreEntry entry, boolean updateExisting, boolean increaseExisting) {
        final String teamKey = createTeamKey(entry.getUsers());

        if (updateExisting) {
            final HighscoreEntry existingEntry = getEntryByKey(teamKey);
            if (existingEntry != null) {
                if (!increaseExisting) {
                    if (existingEntry.score < entry.score) {
                        existingEntry.score = entry.score;
                    }
                } else {
                    existingEntry.score = existingEntry.score + entry.score;
                }
            } else {
                this.entries.put(teamKey, Lists.newArrayList(entry));
            }
        } else {
            if (this.entries.containsKey(teamKey)) {
                this.entries.get(teamKey).add(entry);
            } else {
                this.entries.put(teamKey, Lists.newArrayList(entry));
            }
        }

        if (this.currentSortedScores != null && (this.currentSortedScores.size() < 50 || this.currentLowestScore < entry.score)) {
            this.currentSortedScores = null;
        }
    }

    private HighscoreEntry getEntryByKey(String teamKey) {
        if (this.entries.containsKey(teamKey)) {
            return this.entries.get(teamKey).stream().findFirst().orElse(null);
        }

        return null;
    }

    private String createTeamKey(List<String> users) {
        final List<String> sortedUsers = Lists.newArrayList(users);
        sortedUsers.sort(Collator.getInstance());

        return String.join(",", users);
    }

    public HighscoreEntry getEntryByTeam(final List<String> users) {
        return this.getEntryByKey(createTeamKey(users));
    }

    public void clear() {
        this.entries.clear();

        if (this.currentSortedScores != null) {
            this.currentSortedScores.clear();
            this.currentSortedScores = null;
        }

        this.currentLowestScore = 0;
        this.lastClearTimestamp = Comet.getTime();
    }

    public void reorder() {
        this.currentSortedScores = null;
        this.currentLowestScore = 0;
    }

    public long getLastClearTimestamp() {
        return lastClearTimestamp;
    }

    public static class HighscoreEntry {
        private List<String> users;
        private int score;

        public HighscoreEntry(List<String> users, int score) {
            this.users = users;
            this.score = score;
        }

        public void incrementScore() {
            this.score++;
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
