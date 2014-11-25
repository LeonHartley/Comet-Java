package com.cometproject.server.game.commands.staff.banning;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.types.Ban;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.moderation.BanDao;

public class MachineBanCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 2) {
            return;
        }

        String username = params[0];
        int length = Integer.parseInt(params[1]);

        Session user = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        if (user == null) {
            // TODO: Use the "player_access" table to allow you to machine ban a user that's not online
            return;
        }

        if (user == client || user.getPlayer().getPermissions().hasPermission("user_unbannable")) {
            return;
        }

        long expire = Comet.getTime() + (length * 3600);

        String uniqueId = user.getUniqueId();

        if (CometManager.getBans().hasBan(uniqueId, BanType.MACHINE)) {
            sendChat("Machine ID: " + uniqueId + " is already banned.", client);
            return;
        }

        int banId = BanDao.createBan(BanType.MACHINE, length, expire, uniqueId, client.getPlayer().getId());
        CometManager.getBans().add(new Ban(banId, uniqueId + "", length == 0 ? length : expire, BanType.MACHINE, ""));

        sendChat("User has been machine ID banned (" + uniqueId + ")", client);

        user.disconnect();
    }

    @Override
    public String getPermission() {
        return "machineban_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.machineban.description");
    }
}
