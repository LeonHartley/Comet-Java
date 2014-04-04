package com.cometproject.server.game.commands.user;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.wired.misc.WiredSquare;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SendFloorItemMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class BuildCommand extends ChatCommand {

    @Override
    public void execute(Session client, String message[]) {
        if(message.length < 1) {
            client.send(AdvancedAlertMessageComposer.compose("Comet Server", "Current build: <b>" + Comet.getBuild() + "</b>"));
        }
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
