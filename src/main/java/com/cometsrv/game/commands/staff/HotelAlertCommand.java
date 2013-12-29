package com.cometsrv.game.commands.staff;

import com.cometsrv.boot.Comet;
import com.cometsrv.config.Locale;
import com.cometsrv.game.commands.ChatCommand;
import com.cometsrv.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometsrv.network.sessions.Session;

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
