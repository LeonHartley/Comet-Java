package com.cometproject.game.commands.staff;

import com.cometproject.boot.Comet;
import com.cometproject.config.Locale;
import com.cometproject.game.GameEngine;
import com.cometproject.game.commands.ChatCommand;
import com.cometproject.game.moderation.types.Ban;
import com.cometproject.game.moderation.types.BanType;
import com.cometproject.network.sessions.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BanCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(params.length != 2) {
            return;
        }

        String username = params[0];
        int length = Integer.parseInt(params[1]);

        Session user = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

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
            statement.setString(3, user.getPlayer().getId() + "");
            statement.setString(4, "");

            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()) {
                GameEngine.getBans().add(new Ban(keys.getInt(1), user.getPlayer().getId() + "", expire, BanType.USER, ""));
            }
        } catch (SQLException e) {
            GameEngine.getLogger().error("Error while banning player: " + username, e);
        }
    }

    @Override
    public String getPermission() {
        return "ban_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.ban.description");
    }
}
