package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;

public class KickCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            return;
        }
        String username = params[1];

        Session playerToKick = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        if (playerToKick.getPlayer().getEntity().getUsername() == client.getPlayer().getEntity().getUsername()) {
            // TODO: put in locale
            client.send(AdvancedAlertMessageComposer.compose("Command error", "You can't kick yourself"));
            return;
        }

        if (playerToKick.getPlayer().getPermissions().hasPermission("user_unkickable")) {
            // TODO: put in locale
            client.send(AdvancedAlertMessageComposer.compose("Command error", "You can't kicked this user"));
            return;
        }

        playerToKick.getPlayer().getEntity().leaveRoom(false, true, true);
        // TOOD: put in locale
        playerToKick.send(AdvancedAlertMessageComposer.compose("Information", "You have been kicked from the room."));


        // TODO: put in locale
        client.send(AdvancedAlertMessageComposer.compose("Command successful", playerToKick.getPlayer().getData().getUsername() + " was kicked from your room!"));
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
