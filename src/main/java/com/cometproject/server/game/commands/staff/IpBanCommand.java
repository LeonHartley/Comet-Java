package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.types.Ban;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.network.sessions.Session;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IpBanCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 2) {
            return;
        }

        String username = params[0];
        int length = Integer.parseInt(params[1]);

        Session user = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        if (user == null) {
            return;
        }

        if (user == client || user.getPlayer().getPermissions().hasPermission("user_unbannable")) {
            return;
        }

        long expire = Comet.getTime() + (length * 36000);

        try {
            //String ipAddress = Comet.getServer().getStorage().getString("SELECT `last_ip` FROM players WHERE id = " + user.getPlayer().getId());

            // retrieve ip directly ??

            String ipAddress = ((InetSocketAddress)user.getChannel().remoteAddress()).getAddress().getHostAddress();

            if(GameEngine.getBans().hasBan(ipAddress)) {
                sendChat("IP: " + ipAddress + " is already banned.", client);
                return;
            }

            PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into bans (`type`, `expire`, `data`, `reason`) VALUES(?, ?, ?, ?);", true);

            statement.setString(1, "ip");
            statement.setLong(2, length == 0 ? 0 : expire);
            statement.setString(3, ipAddress);
            statement.setString(4, "");

            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();

            if (keys.next()) {
                GameEngine.getBans().add(new Ban(keys.getInt(1), user.getPlayer().getId() + "", expire, BanType.IP, ""));
            }

            sendChat("User has been IP banned (IP: " + ipAddress + ")", client);
        } catch (SQLException e) {
            GameEngine.getLogger().error("Error while banning player: " + username, e);
        }

        user.disconnect();
    }

    @Override
    public String getPermission() {
        return "ipban_command";
    }

    @Override
    public String getDescription() {
        return "command.ipban.description";
    }
}
