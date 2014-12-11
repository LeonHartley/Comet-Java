package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class ModToolUserInfoMessageComposer {
    public static Composer compose(PlayerData user, PlayerStatistics stats) {
        Composer msg = new Composer(Composers.ModerationToolUserToolMessageComposer);

        msg.writeInt(user.getId());
        msg.writeString(user.getUsername());
        msg.writeString(user.getFigure());

        msg.writeInt((int) (Comet.getTime() - user.getRegTimestamp()) / 60);
        msg.writeInt((int) (Comet.getTime() - user.getLastVisit()) / 60);

        msg.writeBoolean(PlayerManager.getInstance().isOnline(user.getId()));

        msg.writeInt(stats.getHelpTickets());
        msg.writeInt(stats.getAbusiveHelpTickets());
        msg.writeInt(stats.getCautions());
        msg.writeInt(stats.getBans());
        msg.writeInt(0); // TODO: FInd out what this is
        msg.writeString("N/A"); //trading_lock_expiry_txt

        msg.writeString("N/A"); // TODO: purchase logging
        msg.writeInt(1); // ???
        msg.writeInt(0); // Banned accounts

        // TODO: Find banned accounts using this IP address or linked to this e-mail address (for hotels that use the Habbo ID system)

        msg.writeString("Email: " + user.getEmail());
        msg.writeString("IP Address: " + user.getIpAddress());

        return msg;
    }
}
