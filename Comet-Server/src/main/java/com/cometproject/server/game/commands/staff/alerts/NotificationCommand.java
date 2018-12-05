package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class NotificationCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        String image = "generic";
        String message = "";

        if (params.length > 1) {
            image = params[0];
            message = this.merge(params, 1);
        } else {
            message = this.merge(params);
        }

        globalNotification(image, message, client);

        this.logDesc = "El staff %s ha hecho notification con el parámetro '%p'"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%p", message);
    }

    protected void globalNotification(String image, String message, Session client) {
        NetworkManager.getInstance().getSessions().broadcast(new NotificationMessageComposer(image, message));
    }

    @Override
    public String getPermission() {
        return "notification_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.message", "%message%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.notification.description");
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
