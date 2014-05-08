package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.outgoing.user.details.UserInfoMessageComposer;
import com.cometproject.server.network.messages.types.Composer;

public class LoadProfileMessageComposer {
    public static Composer compose(PlayerData player, PlayerStatistics stats, boolean isMyFriend, boolean hasSentRequest) {
        Composer msg = new Composer(Composers.LoadProfileMessageComposer);

        msg.writeInt(player.getId());
        msg.writeString(player.getUsername());
        msg.writeString(player.getFigure());
        msg.writeString(player.getMotto());

        boolean isTimestamp = false;
        int timestamp = 0;

        try {
            timestamp = Integer.parseInt(player.getRegDate());
            isTimestamp = true;
        } catch(Exception ignored) { }

        msg.writeString(isTimestamp ? UserInfoMessageComposer.getDate(timestamp) : player.getRegDate());
        msg.writeInt(player.getAchievementPoints());
        msg.writeInt(stats.getFriendCount());
        msg.writeBoolean(isMyFriend);
        msg.writeBoolean(hasSentRequest);
        msg.writeBoolean(Comet.getServer().getNetwork().getSessions().isPlayerLogged(player.getId()));
        msg.writeInt(0); // groups

        msg.writeInt((int) Comet.getTime() - player.getLastVisit());
        msg.writeBoolean(true);

        return msg;
    }
}
