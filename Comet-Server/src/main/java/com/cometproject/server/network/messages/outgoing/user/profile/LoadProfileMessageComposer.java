package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.UserObjectMessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.ArrayList;
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
        return Composers.ProfileInformationMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
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

        List<GroupData> groups = new ArrayList<>();

        if (this.groups != null) {
            for (int groupId : this.groups) {
                GroupData group = GroupManager.getInstance().getData(groupId);

                if (group != null) {
                    groups.add(group);
                }
            }
        }

        msg.writeInt(groups.size());

        for (GroupData group : groups) {
            if (group != null) {
                msg.writeInt(group.getId());
                msg.writeString(group.getTitle());
                msg.writeString(group.getBadge());
                msg.writeString(group.getColourA());
                msg.writeString(group.getColourB());
                msg.writeBoolean(player.getFavouriteGroup() == group.getId());
                msg.writeInt(-1);
                msg.writeBoolean(group.hasForum());
            }
        }

        groups.clear();

        msg.writeInt((int) Comet.getTime() - player.getLastVisit());
        msg.writeBoolean(true);
    }
}
