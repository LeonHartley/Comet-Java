package com.cometproject.server.game.commands.user.settings;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class ToggleEventsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {

        final PlayerSettings playerSettings = client.getPlayer().getSettings();

        if (playerSettings == null) {
            return;
        }

        playerSettings.setIgnoreInvites(!playerSettings.ignoreEvents());

        final String msg = playerSettings.ignoreEvents() ? "You are now ignoring event notifications!" : "Event notifications are now enabled.";
        sendNotif(Locale.getOrDefault("command.toggleevents.msg." + playerSettings.ignoreEvents(), msg), client);

        PlayerDao.saveIgnoreEvents(playerSettings.ignoreEvents(), client.getPlayer().getId());
    }

    @Override
    public String getPermission() {
        return "ignoreevents_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.toggleevents.description");
    }
}
