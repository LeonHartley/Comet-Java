package com.cometproject.server.game.commands.gimmicks;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.RandomUtil;


public class SlapCommand extends ChatCommand {
    private final static String[] objects = {
            "a large black dildo",
            "a rotten fish",
            "a large trout",
            "a big black boot",
            "the back of %g hand",
            "%g floppy shlong"
    };

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.user.invalid", "Invalid username!"), client);
            return;
        }

        String slappedPlayer = params[0];
        String object = objects[RandomUtil.getRandomInt(0, objects.length - 1)].replace("%g", client.getPlayer().getData().getGender().toLowerCase().equals("m") ? "his" : "her");

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), "* " + client.getPlayer().getData().getUsername() + " slapped " + slappedPlayer + " with " + object + " *", 34));
    }

    @Override
    public String getPermission() {
        return "slap_command";
    }
    
    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.slap.description");
    }
}
