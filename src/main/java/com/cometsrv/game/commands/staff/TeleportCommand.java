package com.cometsrv.game.commands.staff;

import com.cometsrv.config.Locale;
import com.cometsrv.game.commands.ChatCommand;
import com.cometsrv.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometsrv.network.sessions.Session;

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
