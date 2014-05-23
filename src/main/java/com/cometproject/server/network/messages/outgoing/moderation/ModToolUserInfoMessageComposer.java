package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModToolUserInfoMessageComposer {
    public static Composer compose(PlayerData user, PlayerStatistics stats) {
        Composer msg = new Composer(Composers.ModToolUserInfoMessageComposer);

        msg.writeInt(user.getId());
        msg.writeString(user.getUsername());
        msg.writeString(user.getFigure());

        msg.writeInt((int) (Comet.getTime() - user.getRegTimestamp()) / 60);
        msg.writeInt((int) (Comet.getTime() - user.getLastVisit()) / 60);

        msg.writeBoolean(CometManager.getPlayers().isOnline(user.getId()));

        msg.writeInt(stats.getHelpTickets());
        msg.writeInt(stats.getAbusiveHelpTickets());
        msg.writeInt(stats.getCautions());
        msg.writeInt(stats.getBans());
        msg.writeInt(0); // Trade locks

        msg.writeString("N/A"); // TODO: purchase logging
        msg.writeInt(0); // ???
        msg.writeInt(0); // banned accts ???
        msg.writeString(user.getEmail());

        return msg;
    }
}
