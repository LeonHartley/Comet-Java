package com.cometproject.server.game.achievements;

import com.cometproject.server.storage.queries.achievements.AchievementDao;
import com.cometproject.server.utilities.Initializable;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class AchievementManager implements Initializable {
    private static AchievementManager achievementManager;
    private static final Logger log = Logger.getLogger(AchievementManager.class.getName());

    private final Map<String, AchievementGroup> achievementGroups;

    public AchievementManager() {
        this.achievementGroups = new HashMap<>();
    }

    @Override
    public void initialize() {
        this.loadAchievements();
    }

    public void loadAchievements() {
        if(this.achievementGroups.size() != 0) {
            for(AchievementGroup achievementGroup : this.achievementGroups.values()) {
                if(achievementGroup.getAchievements().size() != 0) {
                    achievementGroup.getAchievements().clear();
                }
            }

            this.achievementGroups.clear();
        }

        final int achievementCount = AchievementDao.getAchievements(this.achievementGroups);

        log.info("Loaded " + achievementCount + " achievements (" + this.achievementGroups.size() + " groups)");

    }

    public AchievementGroup getAchievementGroup(String groupName) {
        return this.achievementGroups.get(groupName);
    }

    public static AchievementManager getInstance() {
        if(achievementManager == null) {
            achievementManager = new AchievementManager();
        }

        return achievementManager;
    }
}
