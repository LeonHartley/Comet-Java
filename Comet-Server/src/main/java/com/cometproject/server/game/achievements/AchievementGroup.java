package com.cometproject.server.game.achievements;

import com.cometproject.server.game.achievements.types.Achievement;
import com.cometproject.server.game.achievements.types.AchievementCategory;

import java.util.Map;

public class AchievementGroup {
    private Map<Integer, Achievement> achievements;

    private String groupName;
    private AchievementCategory category;

    public AchievementGroup(Map<Integer, Achievement> achievements, String groupName, AchievementCategory category) {
        this.achievements = achievements;
        this.groupName = groupName;
        this.category = category;
    }

    public Map<Integer, Achievement> getAchievements() {
        return achievements;
    }

    public String getGroupName() {
        return groupName;
    }

    public AchievementCategory getCategory() {
        return category;
    }
}