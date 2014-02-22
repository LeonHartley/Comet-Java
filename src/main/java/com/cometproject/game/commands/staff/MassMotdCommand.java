package com.cometproject.game.commands.staff;

import com.cometproject.boot.Comet;
import com.cometproject.config.Locale;
import com.cometproject.game.commands.ChatCommand;
import com.cometproject.network.messages.outgoing.misc.MotdNotificationComposer;
import com.cometproject.network.sessions.Session;

public class MassMotdCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] message) {
        for(Session c : Comet.getServer().getNetwork().getSessions().getSessions().values()) {
            c.send(MotdNotificationComposer.compose(this.merge(message) + "\n\n- " + client.getPlayer().getData().getUsername()));
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
