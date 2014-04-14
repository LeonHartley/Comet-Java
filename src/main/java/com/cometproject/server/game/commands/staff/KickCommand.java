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
        if(playerToKick == null)
            return;

        if (playerToKick.getPlayer().getEntity().getUsername() == client.getPlayer().getEntity().getUsername()) {

            client.send(AdvancedAlertMessageComposer.compose(Locale.get("command.kick.error"), Locale.get("command.disconnect.himself")));
            return;
        }

        if (playerToKick.getPlayer().getPermissions().hasPermission("user_unkickable")) {

            client.send(AdvancedAlertMessageComposer.compose(Locale.get("command.kick.error"), Locale.get("command.disconnect.undisconnectable")));
            return;
        }

        playerToKick.getPlayer().getEntity().leaveRoom(false, true, true);
        playerToKick.send(AdvancedAlertMessageComposer.compose(Locale.get("command.kick.successtitle"), Locale.get("command.kick.successmessage")));



        client.send(AdvancedAlertMessageComposer.compose(Locale.get("command.kick.successtitle"), Locale.get("command.kick.userkicked").replace("%username%", playerToKick.getPlayer().getData().getUsername())));
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
