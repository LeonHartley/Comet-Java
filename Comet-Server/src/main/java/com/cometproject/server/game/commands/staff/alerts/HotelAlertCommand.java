package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class HotelAlertCommand extends ChatCommand {
    private String logDesc;

    @Override
    public void execute(Session client, String[] message) {
        if (message.length == 0) {
            return;
        }

        NetworkManager.getInstance().getSessions().broadcast(new AlertMessageComposer(this.merge(message) + "<br><br>- " + client.getPlayer().getData().getUsername()));

        this.logDesc = "El Staff -c ha mandado una alerta a todo el hotel. [-s]"
                .replace("-c", client.getPlayer().getData().getUsername())
                .replace("-s", this.merge(message));
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String getPermission() {
        return "hotelalert_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.message", "%message%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.hotelalert.description");
    }

    @Override
    public String getLoggableDescription() {
        return this.logDesc;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }
}
