package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class EventWonCommand extends NotificationCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            return;
        }

        this.globalNotification(Locale.get("command.eventwon.image"),
                Locale.get("command.eventwon.message").replace("%user%", params[0]), client);

        this.logDesc = "El staff %s ha usadoeventwon con el usuario '%b'"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", params[0]);
    }

    @Override
    public String getPermission() {
        return "eventwon_command";
    }

    @Override
    public String getParameter() {
        return "%user%";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.eventwon.description");
    }

    @Override
    public String getLoggableDescription(){
        return this.logDesc;
    }

    @Override
    public boolean isLoggable(){
        return true;
    }
}
