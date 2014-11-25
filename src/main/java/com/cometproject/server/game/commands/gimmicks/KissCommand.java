package com.cometproject.server.game.commands.gimmicks;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.RandomInteger;

public class KissCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(params.length != 1) return;

        String kissedPlayer = params[0];

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(WisperMessageComposer.compose(client.getPlayer().getEntity().getId(), "* " + client.getPlayer().getData().getUsername() + " snogs " + kissedPlayer + " *", 34));
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(ActionMessageComposer.compose(client.getPlayer().getEntity().getId(), 2));

    }

    @Override
    public String getPermission() {
        return "kiss_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.kiss.description");
    }
}
