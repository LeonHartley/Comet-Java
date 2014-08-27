package com.cometproject.server.game.commands.staff;


import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class PointsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2)
            return;
        String username = params[0];
        int points;

        try {
            points = Integer.parseInt(params[1]);
        } catch (Exception e) {
            return;
        }

        Session player = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        if(player == null)
            return;

        player.getPlayer().getData().increasePoints(points);

        player.send(AdvancedAlertMessageComposer.compose(Locale.get("command.points.successtitle"), Locale.get("command.points.successmessage").replace("%amount%", String.valueOf(points))));
        player.getPlayer().sendBalance();
    }

    @Override
    public String getPermission() {
        return "points_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.points.description");
    }
}
