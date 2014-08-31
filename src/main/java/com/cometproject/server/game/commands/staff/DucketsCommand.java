package com.cometproject.server.game.commands.staff;


import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class DucketsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2)
            return;

        String username = params[0];
        int duckets;

        try {
            duckets = Integer.parseInt(params[1]);
        } catch (Exception e) {
            return;
        }

        Session session = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        if(session == null) {
            return;
        }

        session.getPlayer().getData().increaseActivityPoints(duckets);
        session.getPlayer().getData().save();

        session.send(AdvancedAlertMessageComposer.compose(
                Locale.get("command.duckets.successtitle"),
                Locale.get("command.duckets.successmessage").replace("%amount%", String.valueOf(duckets))
        ));

        session.send(session.getPlayer().composeCurrenciesBalance());
    }

    @Override
    public String getPermission() {
        return "duckets_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.duckets.description");
    }
}
