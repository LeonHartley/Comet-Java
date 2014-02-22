package com.cometproject.game.commands.vip;

import com.cometproject.config.Locale;
import com.cometproject.game.commands.ChatCommand;
import com.cometproject.game.rooms.avatars.effects.UserEffect;
import com.cometproject.game.rooms.entities.types.PlayerEntity;
import com.cometproject.network.sessions.Session;

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
            PlayerEntity entity = client.getPlayer().getEntity();
            entity.applyEffect(new UserEffect(effectId, 0));
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
