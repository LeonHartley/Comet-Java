package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class LoadProfileMessageComposer {
    public static Composer compose(PlayerData player, PlayerStatistics stats, boolean isMyFriend, boolean hasSentRequest) {
        Composer msg = new Composer(Composers.LoadProfileMessageComposer);

        msg.writeInt(player.getId());
        msg.writeString(player.getUsername());
        msg.writeString(player.getFigure());
        msg.writeString(player.getMotto());
        msg.writeString(player.getRegDate());
        msg.writeInt(player.getAchievementPoints()); // TODO: achievement score
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
