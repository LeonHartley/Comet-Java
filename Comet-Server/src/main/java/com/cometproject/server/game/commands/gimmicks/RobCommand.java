package com.cometproject.server.game.commands.gimmicks;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.RandomUtil;


public class RobCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.user.invalid", "Invalid username!"), client);
            return;
        }

        String object;

        String robbedPlayer = params[0];
        if(RandomUtil.getRandomInt(0, 100) <= 50) {
            object = Locale.getOrDefault("command.rob.success", "%s robbed %b")
                    .replace("%s", client.getPlayer().getData().getUsername())
                    .replace("%b", robbedPlayer);
        } else {
            object = Locale.getOrDefault("command.rob.failed", "%s tried to rob %b but he couldn't.")
                    .replace("%s", client.getPlayer().getData().getUsername())
                    .replace("%b", robbedPlayer);
        }

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), object, 34));
    }

    @Override
    public String getPermission() {
        return "rob_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.rob.description");
    }
}
