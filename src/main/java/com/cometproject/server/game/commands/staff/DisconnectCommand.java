package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class DisconnectCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        String username = params[1];

        Session userToDisconnect = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        if(userToDisconnect == null) {
            return;
        }

        if (userToDisconnect == client) {
            // TODO: put in locale
            client.send(AdvancedAlertMessageComposer.compose("Command error", "You cannot disconnect yourself."));
            return;
        }

        if (userToDisconnect.getPlayer().getPermissions().hasPermission("undisconnectable")) { // Perk to add
            // TODO: put in locale
            client.send(AdvancedAlertMessageComposer.compose("Command error", "You cannot disconnect this player."));
            return;
        }

        userToDisconnect.disconnect();
    }

    @Override
    public String getPermission() {
        return "disconnect_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.disconnect.description");
    }
}
