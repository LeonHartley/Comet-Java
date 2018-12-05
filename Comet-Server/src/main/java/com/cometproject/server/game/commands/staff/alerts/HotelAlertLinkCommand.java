package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class HotelAlertLinkCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2) {
            sendNotif(Locale.getOrDefault("command.hotelalertlink.args", "This command requires at least 2 arguments!"), client);
        }

        final String link = params[0];

        NetworkManager.getInstance().getSessions().broadcast(new AdvancedAlertMessageComposer(Locale.getOrDefault("command.hotelalertlink.title", "Alert"), this.merge(params, 1) + "<br><br>- " + client.getPlayer().getData().getUsername(), Locale.getOrDefault("command.hotelalertlink.buttontitle", "+info"), link, "frank"));

        this.logDesc = "El staff %s ha mandado una alerta con el enlace '%e' y el mensaje '%m'"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%e", link)
                .replace("%m", this.merge(params, 1));
    }

    @Override
    public String getPermission() {
        return "hotelalertlink_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.message", "%message%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.hotelalertlink.description");
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
