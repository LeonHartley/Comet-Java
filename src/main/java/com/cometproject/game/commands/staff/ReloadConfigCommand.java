package com.cometproject.game.commands.staff;

import com.cometproject.boot.Comet;
import com.cometproject.config.Locale;
import com.cometproject.game.commands.ChatCommand;
import com.cometproject.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.network.sessions.Session;

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
