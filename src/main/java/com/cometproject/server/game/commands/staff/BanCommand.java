package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.types.Ban;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.moderation.BanDao;

public class BanCommand extends ChatCommand {
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

        user.disconnect();

        long expire = Comet.getTime() + (length * 3600);
        int banId = BanDao.createBan(BanType.USER, length, expire, user.getPlayer().getId() + "", client.getPlayer().getId());

        CometManager.getBans().add(new Ban(banId, user.getPlayer().getId() + "", length == 0 ? length : expire, BanType.USER, ""));
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
