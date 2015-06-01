package com.cometproject.server.network.messages.outgoing.user.achievements;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.achievements.AchievementGroup;
import com.cometproject.server.game.players.components.AchievementComponent;
import com.cometproject.server.game.players.components.types.achievements.AchievementProgress;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

public class AchievementUnlockedMessageComposer extends MessageComposer {
    private final AchievementProgress achievementProgress;
    private final AchievementGroup achievementGroup;

    public AchievementUnlockedMessageComposer(AchievementProgress achievementProgress, AchievementGroup achievementGroup) {
        this.achievementProgress = achievementProgress;
        this.achievementGroup = achievementGroup;
    }

    @Override
    public short getId() {
        return Composers.AchievementUnlockedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(achievementGroup.getAchievement(achievementProgress.getLevel()).getId());
        msg.writeInt(achievementProgress.getLevel());
        msg.writeInt(144); // TODO: Find out what this is.
        msg.writeString(achievementGroup.getGroupName() + achievementProgress.getLevel());
        msg.writeInt(achievementGroup.getAchievement(achievementProgress.getLevel()).getRewardAchievement());
        msg.writeInt(achievementGroup.getAchievement(achievementProgress.getLevel()).getRewardActivityPoints());
        msg.writeInt(0);
        msg.writeInt(10);
        msg.writeInt(21);
        msg.writeString(achievementProgress.getLevel() > 1 ? achievementGroup.getGroupName() + (achievementProgress.getLevel() - 1) : "");
        msg.writeString(achievementGroup.getCategory().toString().toLowerCase());
        msg.writeBoolean(true);
    }
}
