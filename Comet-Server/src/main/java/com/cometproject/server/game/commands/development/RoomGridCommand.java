package com.cometproject.server.game.commands.development;

import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class RoomGridCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 0) {
            String str = "";


            client.send(new MotdNotificationMessageComposer(str));

        }

        client.send(new AdvancedAlertMessageComposer("Entity Grid", client.getPlayer().getEntity().getRoom().getMapping().visualiseEntityGrid()));
    }

    @Override
    public String getPermission() {
        return "debug";
    }

    @Override
    public String getParameter() {
        return "";
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
