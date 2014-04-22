package com.cometproject.server.network.messages.outgoing.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModToolUserInfoMessageComposer {
    public static Composer compose(ResultSet user, ResultSet stats) throws SQLException {
        Composer msg = new Composer(Composers.ModToolUserInfoMessageComposer);

        msg.writeInt(user.getInt("id"));
        msg.writeString(user.getString("username"));
        msg.writeString(user.getString("figure"));

        msg.writeInt((int) (Comet.getTime() - user.getInt("reg_timestamp")) / 60);
        msg.writeInt((int) (Comet.getTime() - user.getInt("last_online")) / 60);

        msg.writeBoolean(Comet.getServer().getNetwork().getSessions().isPlayerLogged(user.getInt("id")));

        msg.writeInt(stats.getInt("help_tickets"));
        msg.writeInt(stats.getInt("help_tickets_abusive"));
        msg.writeInt(stats.getInt("cautions"));
        msg.writeInt(stats.getInt("bans"));

        msg.writeString("N/A"); // TODO: purchase logging
        msg.writeInt(0); // ???
        msg.writeInt(0); // banned accts ???
        msg.writeString(user.getString("mail"));

        return msg;
    }
}
