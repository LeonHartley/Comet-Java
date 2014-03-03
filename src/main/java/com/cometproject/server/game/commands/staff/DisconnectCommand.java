package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

/**
 * Created by Julien on 03/03/2014.
 */
public class DisconnectCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        String username = params[1];
        Session UserToDisconnect = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);
        if (UserToDisconnect == client) {
            client.send(AdvancedAlertMessageComposer.compose("Command error", "You cannot disconnect yourself."));
            return;
        }
        if (UserToDisconnect.getPlayer().getPermissions().hasPermission("undisconnectable")) { // Perk to add
            client.send(AdvancedAlertMessageComposer.compose("Command error", "You cannot disconnect this player."));
            return;
        }
        UserToDisconnect.disconnect();
        return;
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
