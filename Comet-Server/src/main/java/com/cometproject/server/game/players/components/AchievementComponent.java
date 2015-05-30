package com.cometproject.server.game.players.components;

import com.cometproject.server.game.achievements.AchievementGroup;
import com.cometproject.server.game.achievements.AchievementManager;
import com.cometproject.server.game.achievements.types.Achievement;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.players.components.types.achievements.AchievementProgress;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.storage.queries.achievements.PlayerAchievementDao;

import java.util.Map;

public class AchievementComponent implements PlayerComponent {
    private final Player player;
    private Map<AchievementType, AchievementProgress> progression;

    public AchievementComponent(Player player) {
        this.player = player;

        this.loadAchievements();
    }

    public void loadAchievements() {
        if(this.progression != null) {
            this.progression.clear();
        }

        this.progression = PlayerAchievementDao.getAchievementProgress(this.player.getId());
    }

    public void progressAchievement(AchievementType type, int data) {
        AchievementGroup achievementGroup = AchievementManager.getInstance().getAchievementGroup(type);

        if(achievementGroup == null) {
            return;
        }

        AchievementProgress progress;

        if(this.progression.containsKey(type)) {
            progress = this.progression.get(type);
        } else {
            progress = new AchievementProgress(1, 0);
        }

        progress.increaseProgress(data);

        Achievement achievement = achievementGroup.getAchievement(progress.getLevel());

        if(achievement.getProgressNeeded() <= progress.getProgress()) {
            // Achievement unlocked!

            // Give badge.
            this.player.getInventory().achievementBadge(type.getGroupName(), progress.getLevel());

            int overflow = progress.getProgress() - achievement.getProgressNeeded();

            if(overflow >= 1) {
                // If we've gone over the requirement, we wanna pass on this progress to the next level.
                progress.setProgress(overflow);
            }

            if(achievementGroup.getAchievement(achievement.getLevel() + 1) != null) {
                progress.increaseLevel();
            }
        }

        PlayerAchievementDao.saveProgress(this.player.getId(), type, progress);
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void dispose() {
        this.progression.clear();
    }
}
