package com.cometproject.server.network.messages.outgoing.user.achievements;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.achievements.AchievementGroup;
import com.cometproject.server.game.achievements.AchievementManager;
import com.cometproject.server.game.achievements.types.Achievement;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.players.components.AchievementComponent;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

import java.util.Map;

public class AchievementsListMessageComposer extends MessageComposer {

    private final AchievementComponent achievementComponent;

    public AchievementsListMessageComposer(final AchievementComponent achievementComponent) {
        this.achievementComponent = achievementComponent;
    }

    @Override
    public short getId() {
        return Composers.AchievementsListMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(AchievementManager.getInstance().getAchievementGroups().size());

        for (Map.Entry<AchievementType, AchievementGroup> entry : AchievementManager.getInstance().getAchievementGroups().entrySet()) {
            final boolean hasStartedAchievement = this.achievementComponent.getProgress(entry.getKey()) != null;
            final Achievement achievement = hasStartedAchievement ?
                    entry.getValue().getAchievement(this.achievementComponent.getProgress(entry.getKey()).getLevel()) :
                    entry.getValue().getAchievement(1);

            msg.writeInt(achievement.getId());
            msg.writeInt(achievement.getLevel());
            msg.writeString(entry.getKey().getGroupName() + achievement.getLevel()); // badge name
            msg.writeInt(achievement.getLevel() != 1 ? entry.getValue().getAchievement(achievement.getLevel() - 1).getProgressNeeded() : achievement.getProgressNeeded());
            msg.writeInt(achievement.getProgressNeeded());
            msg.writeInt(achievement.getRewardActivityPoints());
            msg.writeInt(0); // reward currency
            msg.writeInt(hasStartedAchievement ? this.achievementComponent.getProgress(entry.getKey()).getProgress() : 0);//current progress
            msg.writeBoolean(hasStartedAchievement && entry.getValue().getAchievements().size() <= this.achievementComponent.getProgress(entry.getKey()).getLevel());
            msg.writeString(entry.getValue().getCategory().toString().toLowerCase());
            msg.writeString("");
            msg.writeInt(entry.getValue().getAchievements().size());
            msg.writeInt(0);
        }

        msg.writeString("");
    }
}
