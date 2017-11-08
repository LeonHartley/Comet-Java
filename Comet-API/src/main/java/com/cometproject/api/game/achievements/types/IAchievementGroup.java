package com.cometproject.api.game.achievements.types;

import java.util.Map;

public interface IAchievementGroup {
    int getId();

    int getLevelCount();

    IAchievement getAchievement(int level);

    Map<Integer, IAchievement> getAchievements();

    String getGroupName();

    AchievementCategory getCategory();
}
