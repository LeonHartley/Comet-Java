package com.cometsrv.game.commands.vip;

import com.cometsrv.config.Locale;
import com.cometsrv.game.commands.ChatCommand;
import com.cometsrv.network.sessions.Session;

public class MoonwalkCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(!client.getPlayer().getData().isVip()) {
            this.sendChat("You must be VIP to use this command!", client);
            return;
        }

        /*if(client.getPlayer().getEntity().isMoonwalking) {
            client.getPlayer().getEntity().isMoonwalking = false;
            this.sendChat("Moonwalking is now disabled!", client);
            return;
        }

        client.getPlayer().getEntity().isMoonwalking = true;*/
        this.sendChat("Moonwalking is now enabled!", client);
    }

    @Override
    public String getPermission() {
        return "moonwalk_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.moonwalk.description");
    }
}
