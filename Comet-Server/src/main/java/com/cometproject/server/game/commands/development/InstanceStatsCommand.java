package com.cometproject.server.game.commands.development;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class InstanceStatsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        StringBuilder message = new StringBuilder("<b>Comet Server - Instance Statistics </b><br><br>");

        message.append("Build: " + Comet.getBuild() + "<br><br>");
        message.append("<b>Game Statistics</b><br>Players online: " + PlayerManager.getInstance().size() + "<br>Active rooms: " + RoomManager.getInstance().getRoomInstances().size() + "<br><br>");
        message.append("<b>Room Data</b><br>" + "Cached data instances: " + RoomManager.getInstance().getRoomDataInstances().size() + "<br>" + "<br>" + "<b>Group Data</b><br>" + "Cached data instances: " + GroupManager.getInstance().getGroupData().size() + "<br>" + "Cached instances: " + GroupManager.getInstance().getGroupInstances().size());

        client.send(new AlertMessageComposer(message.toString()));
//
//        final StringBuilder queryStats = new StringBuilder("Queries\n==============================================\n");
//
//        for(Map.Entry<String, AtomicInteger> query : SqlHelper.getQueryCounters().entrySet()) {
//            queryStats.append("\n\nQuery: " + query.getKey()).append("\nCount: " + query.getValue().get());
//        }
//
//        client.send(new MotdNotificationMessageComposer(queryStats.toString()));
    }

    @Override
    public String getPermission() {
        return "dev";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
