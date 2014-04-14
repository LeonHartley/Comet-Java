package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class HotelAlertCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] message) {
        if(message.length == 0) {
            return;
        }

        for(Session c : Comet.getServer().getNetwork().getSessions().getSessions().values()) {
            // TODO: locale
            c.send(AdvancedAlertMessageComposer.compose("Message From Hotel Management", this.merge(message, 0)));
        }
    }

    @Override
    public String getPermission() {
        return "hotelalert_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.hotelalert.description");
    }
}
