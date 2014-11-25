package com.cometproject.server.game.commands.gimmicks;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class PunchCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(params.length != 1) return;

        String punchedPlayer = params[0];

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(WisperMessageComposer.compose(client.getPlayer().getEntity().getId(), "* " + client.getPlayer().getData().getUsername() + " punched " + punchedPlayer + " *", 34));
    }

    @Override
    public String getPermission() {
        return "punch_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.punch.description");
    }
}
