package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.outgoing.user.details.UserObjectMessageComposer;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class LoadProfileMessageComposer {
    public static Composer compose(PlayerData player, PlayerStatistics stats, List<Integer> groups, boolean isMyFriend, boolean hasSentRequest) {
        Composer msg = new Composer(Composers.UserProfileMessageComposer);

        msg.writeInt(player.getId());
        msg.writeString(player.getUsername());
        msg.writeString(player.getFigure());
        msg.writeString(player.getMotto());

        boolean isTimestamp = false;
        int timestamp = 0;

        try {
            timestamp = Integer.parseInt(player.getRegDate());
            isTimestamp = true;
        } catch (Exception ignored) {
        }

        msg.writeString(isTimestamp ? UserObjectMessageComposer.getDate(timestamp) : player.getRegDate());
        msg.writeInt(player.getAchievementPoints());
        msg.writeInt(stats.getFriendCount());
        msg.writeBoolean(isMyFriend);
        msg.writeBoolean(hasSentRequest);
        msg.writeBoolean(PlayerManager.getInstance().isOnline(player.getId()));

        msg.writeInt(groups.size());

        for (Integer groupId : groups) {
            Group group = GroupManager.getInstance().get(groupId);

            msg.writeInt(groupId);
            msg.writeString(group.getData().getTitle());
            msg.writeString(group.getData().getBadge());
            msg.writeString(group.getData().getColourA());
            msg.writeString(group.getData().getColourB());
            msg.writeBoolean(player.getFavouriteGroup() == groupId);
            msg.writeInt(-1);
            msg.writeBoolean(true); // has forum
        }

        msg.writeInt((int) Comet.getTime() - player.getLastVisit());
        msg.writeBoolean(true);

        return msg;
    }
}
