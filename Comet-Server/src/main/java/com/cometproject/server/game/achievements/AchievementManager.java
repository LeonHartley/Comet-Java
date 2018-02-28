package com.cometproject.server.game.achievements;

import com.cometproject.api.game.achievements.IAchievementsService;
import com.cometproject.api.game.achievements.types.AchievementType;
import com.cometproject.api.game.achievements.types.IAchievementGroup;
import com.cometproject.server.storage.queries.achievements.AchievementDao;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AchievementManager implements IAchievementsService {
    private static final Logger log = Logger.getLogger(AchievementManager.class.getName());
    private static AchievementManager achievementManager;
    private final Map<AchievementType, IAchievementGroup> achievementGroups;

    public AchievementManager() {
        this.achievementGroups = new ConcurrentHashMap<>();
    }

    public static AchievementManager getInstance() {
        if (achievementManager == null) {
            achievementManager = new AchievementManager();
        }

        return achievementManager;
    }

    @Override
    public void initialize() {
        this.loadAchievements();

        log.info("AchievementManager initialized");
    }

    @Override
    public void loadAchievements() {
        if (this.achievementGroups.size() != 0) {
            for (IAchievementGroup achievementGroup : this.achievementGroups.values()) {
                if (achievementGroup.getAchievements().size() != 0) {
                    achievementGroup.getAchievements().clear();
                }
            }

            this.achievementGroups.clear();
        }

        final int achievementCount = AchievementDao.getAchievements(this.achievementGroups);

        log.info("Loaded " + achievementCount + " achievements (" + this.achievementGroups.size() + " groups)");

    }

    @Override
    public IAchievementGroup getAchievementGroup(AchievementType groupName) {
        return this.achievementGroups.get(groupName);
    }

    @Override
    public Map<AchievementType, IAchievementGroup> getAchievementGroups() {
        return this.achievementGroups;
    }
}
