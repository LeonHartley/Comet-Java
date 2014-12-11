package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.sessions.Session;


public class MassMotdCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] message) {
        Composer msg = MotdNotificationComposer.compose(this.merge(message) + "\n\n- " + client.getPlayer().getData().getUsername());

        NetworkManager.getInstance().getSessions().broadcast(msg);
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
