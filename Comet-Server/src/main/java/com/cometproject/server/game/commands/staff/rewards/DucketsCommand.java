package com.cometproject.server.game.commands.staff.rewards;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.UpdateActivityPointsMessageComposer;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang.StringUtils;


public class DucketsCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2 || !StringUtils.isNumeric(params[1]))
            return;

        String username = params[0];
        int duckets = Integer.parseInt(params[1]);

        Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (session == null) {
            return;
        }

        session.getPlayer().getData().increaseActivityPoints(duckets);
        session.getPlayer().getData().save();

        session.send(new AdvancedAlertMessageComposer(
                Locale.get("command.duckets.successtitle"),
                Locale.get("command.duckets.successmessage").replace("%amount%", String.valueOf(duckets))
        ));

        session.send(new UpdateActivityPointsMessageComposer(session.getPlayer().getData().getActivityPoints(), duckets));
        session.send(session.getPlayer().composeCurrenciesBalance());

        this.logDesc = "El staff %s ha dado %c duckets al usuario '%u'"
                .replace("%c", Integer.toString(duckets))
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", username);
    }

    @Override
    public String getPermission() {
        return "duckets_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username" + " " + "command.parameter.amount", "%username% %amount%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.duckets.description");
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
