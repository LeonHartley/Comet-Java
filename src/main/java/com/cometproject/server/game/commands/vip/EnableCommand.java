package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.entities.effects.UserEffect;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.network.sessions.Session;

public class EnableCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length == 0) {
            return;
        }

        try {
            int effectId = Integer.parseInt(params[0]);
            PlayerEntity entity = client.getPlayer().getEntity();
            entity.applyEffect(new UserEffect(effectId, 0));

        } catch (Exception e) {
            sendChat(Locale.get("command.enable.invalidid"), client);
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
