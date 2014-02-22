package com.cometproject.game.commands.user;

import com.cometproject.boot.Comet;
import com.cometproject.config.Locale;
import com.cometproject.game.commands.ChatCommand;
import com.cometproject.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.network.sessions.Session;

public class BuildCommand extends ChatCommand {

    @Override
    public void execute(Session client, String message[]) {
        client.send(AdvancedAlertMessageComposer.compose("Comet Server", "Current build: <b>" + Comet.getBuild() + "</b>"));
    }

    @Override
    public String getPermission() {
        return "build_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.build.description");
    }
}
