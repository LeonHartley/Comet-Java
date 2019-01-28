package com.cometproject.server.game.commands.staff.muting;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.ws.messages.alerts.MutedMessage;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class MuteCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 2) {
            sendNotif(Locale.getOrDefault("command.mute.none", "Who do you want to mute?"), client);
            return;
        }

        String username = params[0];

        int playerId = PlayerManager.getInstance().getPlayerIdByUsername(username);
        Session user = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (user == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (user.getPlayer().getPermissions().getRank().roomFilterBypass()) {
            sendNotif(Locale.getOrDefault("command.mute.unmutable", "You can't mute this player!"), client);
            return;
        }

        try {
            int time = Integer.parseInt(params[1]);

            if (time < 0) {
                sendNotif(Locale.getOrDefault("command.mute.negative", "You can only use positive numbers!"), client);
                return;
            }

            final int timeMuted = (int) Comet.getTime() + time * 60;

            PlayerDao.addTimeMute(playerId, timeMuted);
            user.getPlayer().getData().setTimeMuted(timeMuted);

            final String msg = Locale.getOrDefault("command.mute.muted", "You are muted for violating the rules! Your mute will expire in %timeleft% minutes").replace("%timeleft%", time + "");

            if (user.getWsChannel() != null) {
                user.sendWs(new MutedMessage(MutedMessage.MuteType.MODERATOR_MUTE, true, msg));
            } else {
                user.send(new AdvancedAlertMessageComposer(msg));
            }

            isExecuted(client);

            this.logDesc = "%s muted player '%u' for %t minutes"
                    .replace("%s", client.getPlayer().getData().getUsername())
                    .replace("%u", user.getPlayer().getData().getUsername())
                    .replace("%t", Integer.toString(time));
        } catch (Exception e) {
            sendNotif(Locale.getOrDefault("command.mute.invalid", "Please, use numbers only!"), client);
        }

    }

    @Override
    public String getPermission() {
        return "mute_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.mute", "%username% %time%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.mute.description");
    }

    @Override
    public boolean bypassFilter() {
        return true;
    }

    @Override
    public String getLoggableDescription() {
        return this.logDesc;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }
}
