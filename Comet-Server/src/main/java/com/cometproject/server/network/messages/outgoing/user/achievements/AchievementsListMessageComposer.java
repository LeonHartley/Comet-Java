package com.cometproject.server.network.messages.outgoing.user.achievements;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.achievements.AchievementGroup;
import com.cometproject.server.game.achievements.AchievementManager;
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

        for(Map.Entry<AchievementType, AchievementGroup> entry : AchievementManager.getInstance().getAchievementGroups().entrySet()) {
            msg.writeInt(entry.getValue().getAchievement(1).getId());
            msg.writeInt(1);
            msg.writeString(entry.getKey().getGroupName() + 1); // badge name
            msg.writeInt(entry.getValue().getAchievement(1).getProgressNeeded());
            msg.writeInt(entry.getValue().getAchievement(1).getProgressNeeded());
            msg.writeInt(entry.getValue().getAchievement(1).getRewardActivityPoints());
            msg.writeInt(0); // currency
            msg.writeInt(0);//current progress
            msg.writeBoolean(false);
            msg.writeString(entry.getValue().getCategory().toString().toLowerCase());
            msg.writeString("");
            msg.writeInt(entry.getValue().getAchievements().size());
            msg.writeInt(0);
        }

        msg.writeString("");
    }
}
