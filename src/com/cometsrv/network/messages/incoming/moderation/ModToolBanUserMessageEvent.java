package com.cometsrv.network.messages.incoming.moderation;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.moderation.types.Ban;
import com.cometsrv.game.moderation.types.BanType;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModToolBanUserMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();
        String reason = msg.readString();
        int length = msg.readInt();

        Session user = Comet.getServer().getNetwork().getSessions().getByPlayerId(userId);

        if(user == null) {
            return;
        }

        if(user == client && user.getPlayer().getPermissions().hasPermission("user_unbannable")) {
            return;
        }

        user.disconnect();
        long expire = Comet.getTime() + (length * 36000);

        try {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into bans (`type`, `expire`, `data`, `reason`) VALUES(?, ?, ?, ?);");

            statement.setString(1, "user");
            statement.setLong(2, expire);
            statement.setString(3, userId + "");
            statement.setString(4, reason);

            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()) {
                GameEngine.getBans().add(new Ban(keys.getInt(1), userId + "", expire, BanType.USER, reason));
            }
        } catch (SQLException e) {
            GameEngine.getLogger().error("Error while banning player: " + userId, e);
        }
    }
}
