package com.cometproject.server.game.commands.staff.muting;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class UnmuteCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(params.length != 1) {
            return;
        }

        int playerId = CometManager.getPlayers().getPlayerIdByUsername(params[0]);

        if(playerId != -1) {
            Session session = Comet.getServer().getNetwork().getSessions().getByPlayerId(playerId);

            if(session != null) {
                session.send(AdvancedAlertMessageComposer.compose(Locale.get("command.unmute.unmuted")));
            }

            CometManager.getBans().unmute(playerId);
        }
    }

    @Override
    public String getPermission() {
        return "unmute_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.unmute.name");
    }
}
