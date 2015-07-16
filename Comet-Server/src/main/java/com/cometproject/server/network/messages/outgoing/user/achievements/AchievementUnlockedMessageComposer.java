package com.cometproject.server.network.messages.outgoing.user.achievements;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.achievements.types.Achievement;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class AchievementUnlockedMessageComposer extends MessageComposer {
    private final String achievementCategory;
    private final String achievementName;

    private final int currentAchievementId;
    private final Achievement newAchievement;

    public AchievementUnlockedMessageComposer(final String achievementCategory, String achievementName, int currentAchievementId, Achievement newAchievement) {
        this.achievementCategory = achievementCategory;
        this.achievementName = achievementName;
        this.currentAchievementId = currentAchievementId;
        this.newAchievement = newAchievement;
    }

    @Override
    public short getId() {
        return Composers.AchievementUnlockedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(currentAchievementId);
        msg.writeInt(newAchievement.getLevel());
        msg.writeInt(144); // TODO: Find out what this is.
        msg.writeString(achievementName + (newAchievement.getLevel() - 1));
        msg.writeInt(newAchievement.getRewardAchievement());
        msg.writeInt(newAchievement.getRewardActivityPoints());
        msg.writeInt(0);
        msg.writeInt(10);
        msg.writeInt(21);
        msg.writeString(newAchievement.getLevel() > 1 ? achievementName + (newAchievement.getLevel() - 1) : "");

        msg.writeString(achievementCategory.toLowerCase());
        msg.writeBoolean(true);
    }
}
