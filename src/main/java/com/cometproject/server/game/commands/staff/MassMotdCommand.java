package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.misc.MotdNotificationComposer;
import com.cometproject.server.network.sessions.Session;

public class MassMotdCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] message) {
        for(Session c : Comet.getServer().getNetwork().getSessions().getSessions().values()) {
            c.send(MotdNotificationComposer.compose(this.merge(message, 0) + "\n\n- " + client.getPlayer().getData().getUsername()));
        }
    }

    @Override
    public String getPermission() {
        return "massmotd_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.massmotd.description");
    }
}
