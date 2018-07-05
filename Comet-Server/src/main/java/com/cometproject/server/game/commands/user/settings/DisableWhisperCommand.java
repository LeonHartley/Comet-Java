package com.cometproject.server.game.commands.user.settings;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class DisableWhisperCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        final PlayerSettings playerSettings = client.getPlayer().getSettings();

        playerSettings.setDisableWhisper(!playerSettings.disableWhisper());

        final String msg = playerSettings.disableWhisper() ? "disabled" : "enabled";
        sendNotif(Locale.getOrDefault("command.disablewhisper." + msg, String.format("Whispers are now %s", msg)), client);
        PlayerDao.updateDisableWhisper(playerSettings.disableWhisper(), client.getPlayer().getId());
    }

    @Override
    public String getPermission() {
        return "disablewhisper_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.disablewhisper.description");
    }
}
