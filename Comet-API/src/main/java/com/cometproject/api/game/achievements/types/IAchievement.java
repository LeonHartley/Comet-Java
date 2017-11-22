package com.cometproject.api.game.achievements.types;

public interface IAchievement {
    int getLevel();

    int getRewardActivityPoints();

    int getRewardAchievement();

    int getProgressNeeded();
}
