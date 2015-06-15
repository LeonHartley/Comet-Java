package com.cometproject.server.network.messages.outgoing.user.achievements;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.achievements.AchievementGroup;
import com.cometproject.server.game.achievements.types.Achievement;
import com.cometproject.server.game.players.components.types.achievements.AchievementProgress;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

public class AchievementProgressMessageComposer extends MessageComposer {

    private final AchievementGroup achievementGroup;
    private final AchievementProgress achievementProgress;

    public AchievementProgressMessageComposer(AchievementProgress achievementProgress,AchievementGroup achievementGroup) {
        this.achievementProgress = achievementProgress == null ? null : new AchievementProgress(achievementProgress.getLevel(), achievementProgress.getProgress());
        this.achievementGroup = achievementGroup;
    }

    @Override
    public short getId() {
        return Composers.AchievementProgressMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.achievementGroup.getAchievement(achievementProgress.getLevel()).getId());
        msg.writeInt(this.achievementGroup.getAchievement(achievementProgress.getLevel()).getLevel());
        msg.writeString(this.achievementGroup.getGroupName() + this.achievementGroup.getAchievement(achievementProgress.getLevel()).getLevel());
        msg.writeInt(this.achievementGroup.getAchievement(achievementProgress.getLevel()).getProgressNeeded());
        msg.writeInt(this.achievementGroup.getAchievement(achievementProgress.getLevel()).getProgressNeeded());
        msg.writeInt(this.achievementGroup.getAchievement(achievementProgress.getLevel()).getRewardActivityPoints());
        msg.writeInt(0);
        msg.writeInt(this.achievementProgress.getProgress());
        msg.writeBoolean(this.achievementProgress.getLevel() >= this.achievementGroup.getAchievements().size());
        msg.writeString(this.achievementGroup.getCategory().toString().toLowerCase());
        msg.writeString("");
        msg.writeInt(this.achievementGroup.getAchievements().size());
        msg.writeInt(0);
    }
}
