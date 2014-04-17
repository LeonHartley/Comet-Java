package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class InvisibleCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        client.send(WisperMessageComposer.compose(client.getPlayer().getId(), Locale.get("command.error.disabled")));

        boolean isVisible = false;

        if (!client.getPlayer().getEntity().isVisible()) {
            isVisible = true;
        }

        client.getPlayer().getEntity().updateVisibility(isVisible);

        client.send(WisperMessageComposer.compose(client.getPlayer().getId(), Locale.get("command.invisible." + (isVisible ? "enabled" : "disabled"))));
    }

    @Override
    public String getPermission() {
        return "invisible_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.invisible.description");
    }
}
