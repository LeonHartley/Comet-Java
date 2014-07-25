package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.types.Ban;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.moderation.BanDao;

import java.net.InetSocketAddress;

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

        long expire = Comet.getTime() + (length * 3600);

        String ipAddress = ((InetSocketAddress) user.getChannel().remoteAddress()).getAddress().getHostAddress();

        if (CometManager.getBans().hasBan(ipAddress)) {
            sendChat("IP: " + ipAddress + " is already banned.", client);
            return;
        }

        int banId = BanDao.createBan(length, expire, ipAddress, client.getPlayer().getId());
        CometManager.getBans().add(new Ban(banId, user.getPlayer().getId() + "", expire, BanType.IP, ""));

        sendChat("User has been IP banned (IP: " + ipAddress + ")", client);

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
