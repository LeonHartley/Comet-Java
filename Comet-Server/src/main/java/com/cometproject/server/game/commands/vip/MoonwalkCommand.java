package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;


public class MoonwalkCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (client.getPlayer().getEntity().isMoonwalking()) {
            client.getPlayer().getEntity().setIsMoonwalking(false);

            sendNotif(Locale.get("command.moonwalk.disabled"), client);
            return;
        }

        if(client.getPlayer().getEntity().getMountedEntity() != null) {
            return;
        }

        client.getPlayer().getEntity().setIsMoonwalking(true);

        sendNotif(Locale.get("command.moonwalk.enabled"), client);
    }

    @Override
    public String getPermission() {
        return "moonwalk_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.moonwalk.description");
    }
}
