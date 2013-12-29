package com.cometsrv.game.commands.staff;

import com.cometsrv.boot.Comet;
import com.cometsrv.config.Locale;
import com.cometsrv.game.commands.ChatCommand;
import com.cometsrv.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometsrv.network.sessions.Session;

public class ReloadConfigCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] message) {
        Comet.getServer().loadConfig();

        client.send(AdvancedAlertMessageComposer.compose("Command successful", "Config was successfully reloaded."));
    }

    @Override
    public String getPermission() {
        return "reload_config_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.reload_config.description");
    }
}
