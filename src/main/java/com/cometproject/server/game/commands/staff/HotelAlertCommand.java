package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class HotelAlertCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] message) {
        if (message.length == 0) {
            return;
        }

        Comet.getServer().getNetwork().getSessions().broadcast(AdvancedAlertMessageComposer.compose(Locale.get("command.hotelalert.title"), this.merge(message) + "<br><br><i> " + client.getPlayer().getData().getUsername() + "</i>"));
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
