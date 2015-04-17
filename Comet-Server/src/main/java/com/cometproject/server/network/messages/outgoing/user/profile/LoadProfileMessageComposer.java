package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.outgoing.user.details.UserObjectMessageComposer;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class LoadProfileMessageComposer extends MessageComposer {
    private final PlayerData player;
    private final PlayerStatistics stats;
    private final List<Integer> groups;
    private final boolean isMyFriend;
    private final boolean requestSent;

    public LoadProfileMessageComposer(PlayerData player, PlayerStatistics stats, List<Integer> groups, boolean isMyFriend, boolean hasSentRequest) {
        this.player = player;
        this.stats = stats;
        this.groups = groups;
        this.isMyFriend = isMyFriend;
        this.requestSent = hasSentRequest;
    }

    @Override
    public short getId() {
        return Composers.UserProfileMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
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
        msg.writeBoolean(requestSent);
        msg.writeBoolean(PlayerManager.getInstance().isOnline(player.getId()));

        int groupCount = 0;

        for (int groupId : groups) {
            Group group = GroupManager.getInstance().get(groupId);

            if (group != null) {
                groupCount++;
            }
        }

        msg.writeInt(groupCount);

        for (Integer groupId : groups) {
            Group group = GroupManager.getInstance().get(groupId);

            if(group != null) {
                msg.writeInt(groupId);
                msg.writeString(group.getData().getTitle());
                msg.writeString(group.getData().getBadge());
                msg.writeString(group.getData().getColourA());
                msg.writeString(group.getData().getColourB());
                msg.writeBoolean(player.getFavouriteGroup() == groupId);
                msg.writeInt(-1);
                msg.writeBoolean(false); // has forum
            }
        }

        msg.writeInt((int) Comet.getTime() - player.getLastVisit());
        msg.writeBoolean(true);
    }
}
