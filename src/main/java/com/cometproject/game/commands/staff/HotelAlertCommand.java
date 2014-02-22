package com.cometproject.game.commands.staff;

import com.cometproject.boot.Comet;
import com.cometproject.config.Locale;
import com.cometproject.game.commands.ChatCommand;
import com.cometproject.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.network.sessions.Session;

public class HotelAlertCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] message) {
        if(message.length == 0) {
            return;
        }

        for(Session c : Comet.getServer().getNetwork().getSessions().getSessions().values()) {
            c.send(AdvancedAlertMessageComposer.compose("Message From Hotel Management", this.merge(message)));
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
