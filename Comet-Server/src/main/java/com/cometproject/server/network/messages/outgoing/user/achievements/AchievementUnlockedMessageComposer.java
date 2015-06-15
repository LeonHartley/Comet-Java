package com.cometproject.server.network.messages.outgoing.user.achievements;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.achievements.AchievementGroup;
import com.cometproject.server.game.achievements.types.Achievement;
import com.cometproject.server.game.players.components.AchievementComponent;
import com.cometproject.server.game.players.components.types.achievements.AchievementProgress;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

public class AchievementUnlockedMessageComposer extends MessageComposer {
    private final String achievementCategory;
    private final String achievementName;
    private final int achievementLevel;
    private final Achievement currentAchievement;

    public AchievementUnlockedMessageComposer(final String achievementCategory, String achievementName, int achievementLevel, Achievement currentAchievement) {
        this.achievementCategory = achievementCategory;
        this.achievementName = achievementName;
        this.achievementLevel = achievementLevel;
        this.currentAchievement = currentAchievement;
    }

    @Override
    public short getId() {
        return Composers.AchievementUnlockedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(currentAchievement.getId());
        msg.writeInt(achievementLevel);
        msg.writeInt(144); // TODO: Find out what this is.
        msg.writeString(achievementName + achievementLevel);
        msg.writeInt(currentAchievement.getRewardAchievement());
        msg.writeInt(currentAchievement.getRewardActivityPoints());
        msg.writeInt(0);
        msg.writeInt(10);
        msg.writeInt(21);
        msg.writeString(achievementLevel > 1 ? achievementName + (achievementLevel - 1) : "");
        msg.writeString(achievementCategory.toLowerCase());
        msg.writeBoolean(false);
    }
}
