package com.cometproject.api.game.achievements;

import com.cometproject.api.game.achievements.types.AchievementType;
import com.cometproject.api.game.achievements.types.IAchievementGroup;
import com.cometproject.api.utilities.Initialisable;

import java.util.Map;

public interface IAchievementsService extends Initialisable {
    void loadAchievements();

    IAchievementGroup getAchievementGroup(AchievementType groupName);

    Map<AchievementType, IAchievementGroup> getAchievementGroups();
}
