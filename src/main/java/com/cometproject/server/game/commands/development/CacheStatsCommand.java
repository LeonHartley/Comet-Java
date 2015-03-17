package com.cometproject.server.game.commands.development;

import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class CacheStatsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        client.send(new AlertMessageComposer("<b>Cache Statistics</b><br><br>" + "<b>Room Data</b><br>" + "Cached data instances: " + RoomManager.getInstance().getRoomDataInstances().size() + "<br>" + "<br>" + "<b>Group Data</b><br>" + "Cached data instances: " + GroupManager.getInstance().getGroupData().size() + "<br>" + "Cached instances: " + GroupManager.getInstance().getGroupInstances().size()));
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
