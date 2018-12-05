package com.cometproject.server.game.commands.staff.banning;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.user.purse.SendCreditsMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class UnBanCommand extends ChatCommand {
    private String logDesc;

    @Override
    public void execute(Session client, String[] params) {
        if (params.length == 0) {
            sendNotif(Locale.getOrDefault("command.params.length", "Oops! You did something wrong!"), client);
            return;
        }

        String username = params[0];
        Integer PlayerId = PlayerDao.getIdByUsername(username);

        if(BanManager.getInstance().unBan(PlayerId.toString())) {
            sendNotif(Locale.getOrDefault("command.unban.success", "You unbanned %s successfully!")
                    .replace("%s", username), client);
        } else {
            sendNotif(Locale.getOrDefault("command.unban.notbanned", "Oops! Maybe this user isn't banned or has machine ban."), client);
        }

        this.logDesc = "El Staff -c ha desbaneado a -d"
                .replace("-c", client.getPlayer().getData().getUsername())
                .replace("-d", username);
    }

    @Override
    public String getPermission() {
        return "unban_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.unban", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.unban.description");
    }

    @Override
    public String getLoggableDescription() {
        return this.logDesc;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }
}
