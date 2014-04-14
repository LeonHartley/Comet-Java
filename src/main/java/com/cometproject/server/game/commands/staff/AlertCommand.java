package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class AlertCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        // TODO: FIX YOUR SHIT JULIEN FUCK

        Session user = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(params[0]);

        user.send(AdvancedAlertMessageComposer.compose("Alert", this.merge(params, 1)));

    }

    @Override
    public String getPermission() {
        return "alert_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.alert.description");
    }
}
