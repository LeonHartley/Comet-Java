package com.cometproject.server.game.commands.staff.muting;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.boot.Comet;


public class MuteCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 2) {
            sendNotif(Locale.getOrDefault("command.mute.none", "Who do you want to mute?"), client);
            return;
        }

        String username = params[0];

        int playerId = PlayerManager.getInstance().getPlayerIdByUsername(username);
        Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);
        PlayerEntity entity = (PlayerEntity) client.getPlayer().getEntity().getRoom().getEntities().getEntityByName(username, RoomEntityType.PLAYER);

        if (session == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (entity.getUsername().equals(client.getPlayer().getData().getUsername())) {
            return;
        }

        if (entity.getPlayer().getPermissions().getRank().roomFullControl()) {
            sendNotif(Locale.getOrDefault("command.mute.unmutable", "You can't mute this player!"), client);
            return;
        }

        try {
            if (playerId != -1) {
                int time = Integer.parseInt(params[1]);

                if (time < 0) {
                    sendNotif(Locale.getOrDefault("command.mute.negative", "You can only use positive numbers!"), client);
                    return;
                } else if (time > 600) {
                    sendNotif(Locale.getOrDefault("command.mute.nomore", "You can only mute someone for no more than 600 seconds! The amount got changed to 600 seconds."), client);
                    time = 600;
                }

                final int timeMuted = (int) Comet.getTime() + time;

                PlayerDao.addTimeMute(playerId, timeMuted);
                session.getPlayer().getData().setTimeMuted(timeMuted);

                session.send(new AdvancedAlertMessageComposer(Locale.getOrDefault("command.mute.muted", "You are muted for violating the rules! Your mute will expire in %timeleft% seconds").replace("%timeleft%", time + "")));
            }
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
}
