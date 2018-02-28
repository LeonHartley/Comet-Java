package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;


public class TeleportCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] message) {
        if (client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId()) &&
                    !client.getPlayer().getPermissions().getRank().roomFullControl()) {
                sendNotif(Locale.getOrDefault("command.need.rights", "You need rights to use this command in this room!"), client);
                return;
            }

            if (client.getPlayer().getEntity().hasAttribute("teleport")) {
                client.getPlayer().getEntity().removeAttribute("teleport");
                sendNotif(Locale.get("command.teleport.disabled"), client);
            } else {
                client.getPlayer().getEntity().setAttribute("teleport", true);
                sendNotif(Locale.get("command.teleport.enabled"), client);
            }
        }
    }

    @Override
    public String getPermission() {
        return "teleport_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.teleport.description");
    }
}
