package com.cometproject.server.game.commands.staff.fun;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang.StringUtils;

public class RollCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) return;

        if (!StringUtils.isNumeric(params[0])) {
            return;
        }

        int number = Integer.parseInt(params[0]);

        if (number < 1) number = 1;
        if (number > 6) number = 6;

        client.getPlayer().getEntity().setAttribute("diceRoll", number);

        this.logDesc = "El staff %s ha hecho roll en la sala '%b' y ha sacado un %r"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", client.getPlayer().getEntity().getRoom().getData().getName())
                .replace("%r", Integer.toString(number));
    }

    @Override
    public String getPermission() {
        return "roll_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.number", "%number%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roll.description");
    }

    @Override
    public boolean isHidden() {
        return true;
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
