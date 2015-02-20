package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class PlayerInfoCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(params.length != 1) return;

        final String username = params[0];
        Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        PlayerData playerData;

        if(session == null) {
            playerData = PlayerDao.getDataByUsername(username);
        } else {
            playerData = session.getPlayer().getData();
        }

        final StringBuilder userInfo = new StringBuilder();

        userInfo.append("<b>ID</b>: " + playerData.getId() + "<br>");
        userInfo.append("<b>Username</b>: " + username + "<br>");
        userInfo.append("<b>Motto</b>: " + playerData.getMotto() + "<br>");
        userInfo.append("<b>Gender</b>: " + (playerData.getGender().toLowerCase().equals("m") ? "Male" : "Female") + "<br>");
        userInfo.append("<b>Status</b>: " + (session == null ? "Offline" : "Online") + "<br>");
        userInfo.append("<b>Achievement Points</b>: " + playerData.getAchievementPoints() + "<br>");
        userInfo.append("<b>Rank</b>: " + playerData.getRank() + "<br><br>");

        userInfo.append("<b>Currency Balances</b><br>");
        userInfo.append("<i>" + playerData.getCredits() + " credits</i><br>");
        userInfo.append("<i>" + playerData.getVipPoints() + " diamonds</i><br>");
        userInfo.append("<i>" + playerData.getActivityPoints() + " duckets</i><br><br>");

        userInfo.append("<b>Room Info</b><br>");
        if(session != null && session.getPlayer().getEntity() != null) {
            userInfo.append("<b>Room ID</b>: " + session.getPlayer().getEntity().getRoom().getData().getId() + "<br>");
            userInfo.append("<b>Room Name</b>: " + session.getPlayer().getEntity().getRoom().getData().getName() + "<br>");
            userInfo.append("<b>Room Owner</b>: " + session.getPlayer().getEntity().getRoom().getData().getOwner() + "<br>");
        } else {
            if(session == null)
                userInfo.append("<i>This player is not online</i>");
            else
                userInfo.append("<i>This player is not in a room</i>");
        }

        client.send(AdvancedAlertMessageComposer.compose("Player Information: " + username, userInfo.toString()));
    }

    @Override
    public String getPermission() {
        return "playerinfo_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.playerinfo.description");
    }
}
