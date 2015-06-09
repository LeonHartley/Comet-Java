package com.cometproject.server.network.messages.outgoing.user.achievements;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.achievements.AchievementGroup;
import com.cometproject.server.game.players.components.AchievementComponent;
import com.cometproject.server.game.players.components.types.achievements.AchievementProgress;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

public class AchievementUnlockedMessageComposer extends MessageComposer {
    private final int achievementLevel;

    private final AchievementGroup achievementGroup;

    public AchievementUnlockedMessageComposer(int achievementLevel, AchievementGroup achievementGroup) {
        this.achievementLevel = achievementLevel;
        this.achievementGroup = achievementGroup;
    }

    @Override
    public short getId() {
        return Composers.AchievementUnlockedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(achievementGroup.getAchievement(achievementLevel).getId());
        msg.writeInt(achievementLevel);
        msg.writeInt(144); // TODO: Find out what this is.
        msg.writeString(achievementGroup.getGroupName() + achievementLevel);
        msg.writeInt(achievementGroup.getAchievement(achievementLevel).getRewardAchievement());
        msg.writeInt(achievementGroup.getAchievement(achievementLevel).getRewardActivityPoints());
        msg.writeInt(0);
        msg.writeInt(10);
        msg.writeInt(21);
        msg.writeString(achievementLevel > 1 ? achievementGroup.getGroupName() + (achievementLevel - 1) : "");
        msg.writeString(achievementGroup.getCategory().toString().toLowerCase());
        msg.writeBoolean(false);
    }
}
