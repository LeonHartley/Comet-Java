package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;

/**
 * Created by Julien on 01/03/2014.
 */
public class KickCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            return;
        }
        String username = params[1];
        Session PlayerToKick = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);
        if (PlayerToKick.getPlayer().getEntity().getUsername() == client.getPlayer().getEntity().getUsername()) {
            client.send(AdvancedAlertMessageComposer.compose("Command error", "You can't kick yourself"));
            return;
        }
        if (PlayerToKick.getPlayer().getPermissions().hasPermission("user_unkickable")) {
            client.send(AdvancedAlertMessageComposer.compose("Command error", "You can't kicked this user"));
            return;
        }
        PlayerToKick.getPlayer().getEntity().leaveRoom(false, true, true);
        PlayerToKick.send(AdvancedAlertMessageComposer.compose("Information", "You have been kicked from the room."));
        client.send(AdvancedAlertMessageComposer.compose("Command successful", PlayerToKick.getPlayer().getData().getUsername() + "was kicked from your room !"));
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
