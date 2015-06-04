package com.cometproject.server.game.players.components;

import com.cometproject.server.game.achievements.AchievementGroup;
import com.cometproject.server.game.achievements.AchievementManager;
import com.cometproject.server.game.achievements.types.Achievement;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.players.components.types.achievements.AchievementProgress;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.network.messages.outgoing.user.achievements.AchievementPointsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.achievements.AchievementProgressMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.achievements.AchievementUnlockedMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.UpdateActivityPointsMessageComposer;
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
        if (this.progression != null) {
            this.progression.clear();
        }

        this.progression = PlayerAchievementDao.getAchievementProgress(this.player.getId());
    }

    public void progressAchievement(AchievementType type, int data) {
        AchievementGroup achievementGroup = AchievementManager.getInstance().getAchievementGroup(type);

        if (achievementGroup == null) {
            return;
        }

        AchievementProgress progress;

        if (this.progression.containsKey(type)) {
            progress = this.progression.get(type);
        } else {
            progress = new AchievementProgress(1, 0);
            this.progression.put(type, progress);
        }

        if (achievementGroup.getAchievements().size() <= progress.getLevel() && achievementGroup.getAchievement(progress.getLevel()).getProgressNeeded() <= progress.getProgress()) {
            return;
        }

        progress.increaseProgress(data);

        Achievement achievement = achievementGroup.getAchievement(progress.getLevel());

        if (achievement.getProgressNeeded() <= progress.getProgress()) {
            this.player.getInventory().achievementBadge(type.getGroupName(), progress.getLevel());

            this.player.getData().increaseAchievementPoints(achievement.getRewardAchievement());
            this.player.getData().increaseActivityPoints(achievement.getRewardActivityPoints());

            this.player.getData().save();
            this.player.poof();

            this.getPlayer().getSession().send(this.getPlayer().composeCurrenciesBalance());
            this.getPlayer().getSession().send(new UpdateActivityPointsMessageComposer(this.getPlayer().getData().getActivityPoints(), achievement.getRewardAchievement()));
            this.getPlayer().getSession().send(new AchievementPointsMessageComposer(this.getPlayer().getData().getAchievementPoints()));

            // Achievement unlocked!
            this.player.getSession().send(new AchievementUnlockedMessageComposer(progress.getLevel(), achievementGroup));

            if (achievementGroup.getAchievement(achievement.getLevel() + 1) != null) {
                progress.increaseLevel();
                progress.setProgress(0);
            }
        }

        this.player.getSession().send(new AchievementProgressMessageComposer(progress, achievementGroup.getAchievement(progress.getLevel()), achievementGroup));
        PlayerAchievementDao.saveProgress(this.player.getId(), type, progress);
    }

    public AchievementProgress getProgress(AchievementType achievementType) {
        return this.progression.get(achievementType);
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
