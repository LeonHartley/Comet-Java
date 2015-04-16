package com.cometproject.server.game.commands.user;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class BuildCommand extends ChatCommand {

    @Override
    public void execute(Session client, String message[]) {
        client.send(new AdvancedAlertMessageComposer("Comet Server", "Current build: <b>" + Comet.getBuild() + "</b>"));
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
