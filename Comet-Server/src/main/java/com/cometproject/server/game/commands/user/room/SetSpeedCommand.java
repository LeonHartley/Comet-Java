package com.cometproject.server.game.commands.user.room;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;


public class SetSpeedCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendWhisper(Locale.getOrDefault("command.setspeed.none", "To change the roller speed type :setspeed %number%"), client);
            return;
        }

        if (client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().getRank().roomFullControl()) {
                sendNotif(Locale.getOrDefault("command.need.rights", "You need rights to use this command!"), client);
                return;
            }

            try {
                int speed = Integer.parseInt(params[0]);

                if (speed < 0) {
                    speed = 3;
                } else if (speed > 20) {
                    speed = 20;
                }

                client.getPlayer().getEntity().getRoom().setAttribute("customRollerSpeed", speed);
                sendNotif(Locale.get("command.setspeed.set").replace("%s", speed + ""), client);
            } catch (Exception e) {
                sendNotif(Locale.getOrDefault("command.setspeed.invalid", "Please, use numbers only!"), client);
            }
        }
    }

    @Override
    public String getPermission() {
        return "setspeed_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.number", "%number%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.setspeed.description");
    }
}
