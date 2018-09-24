package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.network.sessions.Session;


public class EffectCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.enable.none", "To get a effect type :effect %number%"), client);
            return;
        }

        try {
            int effectId = Integer.parseInt(params[0]);

            PlayerEntity entity = client.getPlayer().getEntity();

            if (entity.getCurrentEffect() != null) {
                if (entity.getGameTeam() != null && entity.getGameTeam() != GameTeam.NONE) {
                    return;
                }

                if (entity.getCurrentEffect().isItemEffect()) {
                    return;
                }
            }

            entity.applyEffect(new PlayerEffect(effectId, 0));
        } catch (Exception e) {
            sendNotif(Locale.get("command.enable.invalidid"), client);
        }
    }

    @Override
    public String getPermission() {
        return "enable_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.number", "%number%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.enable.description");
    }

    @Override
    public boolean canDisable() {
        return true;
    }
}
