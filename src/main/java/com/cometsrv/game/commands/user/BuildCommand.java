package com.cometsrv.game.commands.user;

import com.cometsrv.boot.Comet;
import com.cometsrv.config.Locale;
import com.cometsrv.game.commands.ChatCommand;
import com.cometsrv.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometsrv.network.sessions.Session;

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
