package com.cometproject.server.game.commands.staff.rewards;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;


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

        Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (session == null) {
            PlayerData playerData = PlayerDao.getDataByUsername(username);

            if (playerData == null) return;

            playerData.increasePoints(points);
            playerData.save();
            return;
        }

        session.getPlayer().getData().increasePoints(points);
        session.getPlayer().getData().save();

        session.send(new AdvancedAlertMessageComposer(
                Locale.get("command.points.successtitle"),
                Locale.get("command.points.successmessage").replace("%amount%", String.valueOf(points))
        ));

        session.send(session.getPlayer().composeCurrenciesBalance());
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
