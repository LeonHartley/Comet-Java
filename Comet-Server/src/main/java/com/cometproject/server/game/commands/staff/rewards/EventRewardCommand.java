package com.cometproject.server.game.commands.staff.rewards;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.commands.staff.alerts.NotificationCommand;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.api.data.Data;

public class EventRewardCommand extends NotificationCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2)
            return;

        final String username = params[0];
        final String badge = params[1];

        final Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);
        int playerId = session != null ? session.getPlayer().getId() : PlayerManager.getInstance().getPlayerIdByUsername(username);

        if (playerId == -1) {
            playerId = PlayerDao.getIdByUsername(username);

            if (playerId == 0) {
                sendWhisper(Locale.getOrDefault("command.eventreward.notfound", "Could not find that player!"), client);
                return;
            }
        }

        final Data<Boolean> received = Data.createEmpty();
        StorageContext.getCurrentContext().getRewardRepository().playerReceivedReward(playerId, badge, received::set);

        if (received.has() && received.get()) {
            sendWhisper(Locale.getOrDefault("command.eventreward.already_got", "This player has already received this reward!"), client);
        } else {
            final int vipPoints = Integer.parseInt(Locale.getOrDefault("command.eventreward.vip_points", "1"));
            final int seasonalPoints = Integer.parseInt(Locale.getOrDefault("command.eventreward.seasonal_points", "1"));

            StorageContext.getCurrentContext().getRewardRepository().giveReward(playerId, badge, vipPoints, seasonalPoints);

            if (session != null) {
                session.getPlayer().getData().increaseVipPoints(vipPoints);
                session.getPlayer().getData().increaseSeasonalPoints(seasonalPoints);

                session.getPlayer().getInventory().addBadge(badge, false, true);
                session.getPlayer().sendBalance();


                if (Locale.getOrDefault("notification.eventwin.enabled", "true").equals("true")) {
                    globalNotification("generic", Locale.getOrDefault("notification.eventwin",
                            "%username% just won an event!").replace("%username%", session.getPlayer().getData().getUsername()), session);
                }

                sendWhisper(Locale.getOrDefault("command.eventreward.received", "You just received an event reward!"), session);
            }


            sendWhisper(Locale.getOrDefault("command.eventreward.already_got", "Given event reward successfully"), client);
        }

        this.logDesc = "El staff %s ha hecho eventreward al usuario '%b'"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", username);
    }

    @Override
    public String getPermission() {
        return "eventreward_command";
    }

    @Override
    public String getParameter() {
        return "%username% %badge%";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.eventreward.description", "Gives a player a badge & points");
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
