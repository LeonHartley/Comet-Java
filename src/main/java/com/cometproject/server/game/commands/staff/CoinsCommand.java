package com.cometproject.server.game.commands.staff;


import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class CoinsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2)
            return;

        String username = params[0];

        try {
            int credits = Integer.parseInt(params[1]);
            Session player = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

            player.getPlayer().getData().increaseCredits(credits);
            player.send(AdvancedAlertMessageComposer.compose(Locale.get("command.coins.title"), Locale.get("command.coins.received").replace("%amount%", String.valueOf(credits))));

            client.getPlayer().sendBalance();
        } catch (Exception e) {
            client.send(AdvancedAlertMessageComposer.compose(Locale.get("command.coins.errortitle"), Locale.get("command.coins.formaterror")));
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
