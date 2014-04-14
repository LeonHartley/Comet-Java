package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class TeleportCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] message) {
        // TODO: Teleporting
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
