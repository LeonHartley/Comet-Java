package com.cometproject.server.network.messages.outgoing.user.achievements;

import com.cometproject.api.game.achievements.types.IAchievement;
import com.cometproject.api.game.achievements.types.IAchievementGroup;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.Collection;

public class AchievementRequirementsMessageComposer extends MessageComposer {

    private final Collection<IAchievementGroup> achievementGroups;

    public AchievementRequirementsMessageComposer(Collection<IAchievementGroup> achievementGroups) {
        this.achievementGroups = achievementGroups;
    }


    @Override
    public short getId() {
        return Composers.BadgeDefinitionsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.achievementGroups.size());

        for (IAchievementGroup achievementGroup : this.achievementGroups) {
            msg.writeString(achievementGroup.getGroupName().replace("ACH_", ""));
            msg.writeInt(achievementGroup.getAchievements().size());

            for (IAchievement achievement : achievementGroup.getAchievements().values()) {
                msg.writeInt(achievement.getLevel());
                msg.writeInt(achievement.getProgressNeeded());
            }
        }
    }
}
