package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class KickCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {

        if (params.length < 1) {
            return;
        }
        String username = params[1];

        Session playerToKick = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        if (playerToKick == null)
            return;

        if (playerToKick.getPlayer().getEntity().getUsername().equals(client.getPlayer().getEntity().getUsername())) {
            return;
        }

        if (playerToKick.getPlayer().getPermissions().hasPermission("user_unkickable")) {
            sendChat(Locale.get("command.kick.unkickable"), client);
            return;
        }

        playerToKick.getPlayer().getEntity().leaveRoom(false, true, true);
    }

    @Override
    public String getPermission() {
        return "kick_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.kick.description");
    }
}
