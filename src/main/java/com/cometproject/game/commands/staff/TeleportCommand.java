package com.cometproject.game.commands.staff;

import com.cometproject.config.Locale;
import com.cometproject.game.commands.ChatCommand;
import com.cometproject.network.sessions.Session;

public class TeleportCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] message) {
        /*if(client.getPlayer().getEntity().getIsTeleporting()) {
            client.getPlayer().getEntity().setIsTeleporting(false);
            client.send(WisperMessageComposer.compose(client.getPlayer().getId(), Locale.get("command.teleport.disabled")));
        } else {
            client.getPlayer().getEntity().setIsTeleporting(true);
            client.send(WisperMessageComposer.compose(client.getPlayer().getId(), Locale.get("command.teleport.enabled")));
        }*/
    }

    @Override
    public String getPermission() {
        return "teleport_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.teleport.description");
    }
}
