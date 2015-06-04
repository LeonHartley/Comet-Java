package com.cometproject.server.network.messages.outgoing.user.achievements;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.achievements.AchievementGroup;
import com.cometproject.server.game.achievements.types.Achievement;
import com.cometproject.server.game.players.components.types.achievements.AchievementProgress;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

public class AchievementProgressMessageComposer extends MessageComposer {

    private final AchievementGroup achievementGroup;
    private final Achievement achievement;
    private final AchievementProgress achievementProgress;

    public AchievementProgressMessageComposer(AchievementProgress achievementProgress, Achievement achievement, AchievementGroup achievementGroup) {
        this.achievementProgress = achievementProgress == null ? null : new AchievementProgress(achievementProgress.getLevel(), achievementProgress.getProgress());
        this.achievement = achievement;
        this.achievementGroup = achievementGroup;
    }

    @Override
    public short getId() {
        return Composers.AchievementProgressMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.achievement.getId());
        msg.writeInt(this.achievement.getLevel());
        msg.writeString(this.achievementGroup.getGroupName() + this.achievement.getLevel());
        msg.writeInt(this.achievement.getProgressNeeded());
        msg.writeInt(this.achievement.getProgressNeeded());
        msg.writeInt(this.achievement.getRewardActivityPoints());
        msg.writeInt(0);
        msg.writeInt(this.achievementProgress != null ? this.achievementProgress.getProgress() : 0);
        msg.writeBoolean(this.achievementProgress != null && this.achievementProgress.getLevel() >= this.achievementGroup.getAchievements().size());
        msg.writeString(this.achievementGroup.getCategory().toString().toLowerCase());
        msg.writeString("");
        msg.writeInt(this.achievementGroup.getAchievements().size());
        msg.writeInt(0);
    }
}
