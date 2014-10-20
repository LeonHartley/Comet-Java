package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang.StringUtils;

public class SetSpeedCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            return;
        }

        if(!StringUtils.isNumeric(params[0])) return;

        if (client.getPlayer().getEntity() != null
                && client.getPlayer().getEntity().getRoom() != null) {
            if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
                return;
            }

            int speed = Integer.parseInt(params[0]);

            if(speed < 0) {
                speed = 0;
            } else if(speed > 20) {
                speed = 20;
            }

            client.getPlayer().getEntity().getRoom().setAttribute("customRollerSpeed", speed);
            sendChat(Locale.get("command.setspeed.set").replace("%s", speed + ""), client);
        }
    }

    @Override
    public String getPermission() {
        return "setspeed_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.setspeed.description");
    }
}