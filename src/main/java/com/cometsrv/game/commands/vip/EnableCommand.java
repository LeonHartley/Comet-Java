package com.cometsrv.game.commands.vip;

import com.cometsrv.config.Locale;
import com.cometsrv.game.commands.ChatCommand;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.network.sessions.Session;

public class EnableCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(!client.getPlayer().getData().isVip()) {
            this.sendChat("You must be VIP to use this command!", client);
            return;
        }

        if(params.length == 0) {
            return;
        }

        try {
            int effectId = Integer.parseInt(params[0]);
            Avatar avatar = client.getPlayer().getAvatar();
            avatar.applyEffect(effectId);
        } catch(Exception e) {
            this.sendChat("Invalid effect ID", client);
        }
    }

    @Override
    public String getPermission() {
        return "enable_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.enable.description");
    }
}
