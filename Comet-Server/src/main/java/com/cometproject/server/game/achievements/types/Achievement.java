package com.cometproject.server.game.achievements.types;

public class Achievement {
    private final int id;
    private final int level;
    private final int rewardActivityPoints;
    private final int rewardAchievement;
    private final int progressNeeded;

    public Achievement(int id, int level, int rewardActivityPoints, int rewardAchievement, int progressNeeded) {
        this.id = id;
        this.level = level;
        this.rewardActivityPoints = rewardActivityPoints;
        this.rewardAchievement = rewardAchievement;
        this.progressNeeded = progressNeeded;
    }

    public int getLevel() {
        return level;
    }

    public int getRewardActivityPoints() {
        return rewardActivityPoints;
    }

    public int getRewardAchievement() {
        return rewardAchievement;
    }

    public int getProgressNeeded() {
        return progressNeeded;
    }

    public int getId() {
        return id;
    }
}
