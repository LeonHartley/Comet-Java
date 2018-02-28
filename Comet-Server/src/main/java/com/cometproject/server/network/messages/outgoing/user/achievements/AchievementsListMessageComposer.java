package com.cometproject.server.network.messages.outgoing.user.achievements;

import com.cometproject.api.game.achievements.types.AchievementType;
import com.cometproject.api.game.achievements.types.IAchievement;
import com.cometproject.api.game.achievements.types.IAchievementGroup;
import com.cometproject.api.game.players.data.components.achievements.IAchievementProgress;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.achievements.AchievementManager;
import com.cometproject.server.game.players.components.AchievementComponent;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.Map;

public class AchievementsListMessageComposer extends MessageComposer {

    private final AchievementComponent achievementComponent;

    public AchievementsListMessageComposer(final AchievementComponent achievementComponent) {
        this.achievementComponent = achievementComponent;
    }

    @Override
    public short getId() {
        return Composers.AchievementsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(AchievementManager.getInstance().getAchievementGroups().size());

        for (Map.Entry<AchievementType, IAchievementGroup> entry : AchievementManager.getInstance().getAchievementGroups().entrySet()) {
            IAchievementProgress achievementProgress = this.achievementComponent.getProgress(entry.getKey());
            IAchievement achievement = achievementProgress == null ? entry.getValue().getAchievement(1) : entry.getValue().getAchievement(achievementProgress.getLevel());

            msg.writeInt(entry.getValue().getId());
            msg.writeInt(achievement == null ? 0 : achievement.getLevel());
            msg.writeString(achievement == null ? "" : entry.getKey().getGroupName() + achievement.getLevel());
            msg.writeInt(achievement == null ? 0 : achievement.getLevel() == 1 ? 0 : entry.getValue().getAchievement(achievement.getLevel() - 1).getProgressNeeded());
            msg.writeInt(achievement == null ? 0 : achievement.getProgressNeeded());
            msg.writeInt(achievement == null ? 0 : achievement.getRewardActivityPoints());
            msg.writeInt(0);
            msg.writeInt(achievementProgress != null ? achievementProgress.getProgress() : 0);

            if (achievementProgress == null) {
                msg.writeBoolean(false);
            } else if (achievementProgress.getLevel() >= entry.getValue().getLevelCount()) {
                msg.writeBoolean(true);
            } else {
                msg.writeBoolean(false);
            }

            msg.writeString(entry.getValue().getCategory().toString().toLowerCase());
            msg.writeString("");
            msg.writeInt(entry.getValue().getLevelCount());
            msg.writeInt(0);
        }

        msg.writeString("");
    }
}
