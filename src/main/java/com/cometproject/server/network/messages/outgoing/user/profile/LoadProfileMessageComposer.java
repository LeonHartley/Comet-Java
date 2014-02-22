package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class LoadProfileMessageComposer {
    public static Composer compose(PlayerData player, PlayerStatistics stats) {
        Composer msg = new Composer(Composers.LoadProfileMessageComposer);

        msg.writeInt(player.getId());
        msg.writeString(player.getUsername());
        msg.writeString(player.getFigure());
        msg.writeString(player.getMotto());
        msg.writeString(player.getRegDate());
        msg.writeInt(stats.getAchievementPoints()); // TODO: achievement score
        msg.writeInt(0); // TODO: friend count
        msg.writeBoolean(false); // is my friend
        msg.writeBoolean(false); // has request from me
        msg.writeBoolean(Comet.getServer().getNetwork().getSessions().isPlayerLogged(player.getId()));
        msg.writeInt(0); // groups

        msg.writeInt((int) Comet.getTime() - player.getLastVisit());
        msg.writeBoolean(true);

        return msg;
    }
}
