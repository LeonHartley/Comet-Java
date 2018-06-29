package com.cometproject.server.game.commands.staff.rewards.mass;

import com.cometproject.server.config.Locale;


public class MassSeasonalCommand extends MassCurrencyCommand {
    @Override
    public String getPermission() {
        return "mass_seasonal_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.mass.seasonal.description");
    }
}
