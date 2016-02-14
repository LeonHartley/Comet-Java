package com.cometproject.server.game.achievements;

import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.storage.queries.achievements.AchievementDao;
import com.cometproject.server.utilities.Initializable;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AchievementManager implements Initializable {
    private static AchievementManager achievementManager;
    private static final Logger log = Logger.getLogger(AchievementManager.class.getName());

    private final Map<AchievementType, AchievementGroup> achievementGroups;

    public AchievementManager() {
        this.achievementGroups = new ConcurrentHashMap<>();
    }

    @Override
    public void initialize() {
        this.loadAchievements();

        log.info("AchievementManager initialized");
    }

    public void loadAchievements() {
        if (this.achievementGroups.size() != 0) {
            for (AchievementGroup achievementGroup : this.achievementGroups.values()) {
                if (achievementGroup.getAchievements().size() != 0) {
                    achievementGroup.getAchievements().clear();
                }
            }

            this.achievementGroups.clear();
        }

        final int achievementCount = AchievementDao.getAchievements(this.achievementGroups);

        log.info("Loaded " + achievementCount + " achievements (" + this.achievementGroups.size() + " groups)");

    }

    public AchievementGroup getAchievementGroup(AchievementType groupName) {
        return this.achievementGroups.get(groupName);
    }

    public Map<AchievementType, AchievementGroup> getAchievementGroups() {
        return this.achievementGroups;
    }

    public static AchievementManager getInstance() {
        if (achievementManager == null) {
            achievementManager = new AchievementManager();
        }

        return achievementManager;
    }
}
