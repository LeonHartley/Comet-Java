package com.cometproject.server.game.commands.staff;


import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class CoinsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(params.length < 1)
            return;

        // TODO: julien - fix this shit or I'm not gonna be happy.

        String username;
        username = params[0];
        try {
            int Credits = Integer.parseInt(params[1]);
            Session Player = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);
            Player.getPlayer().getData().increaseCredits(Credits);
            Player.send(AdvancedAlertMessageComposer.compose("Alert", "You received " + Credits + " credits"));
            client.getPlayer().sendBalance();
        } catch(Exception Ignored) {
            client.send(AdvancedAlertMessageComposer.compose("Error", "Error in credit format"));
        }

    }

    @Override
    public String getPermission() {
        return "coins_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.coins.description");
    }
}
