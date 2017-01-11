package com.cometproject.server.game.commands.gimmicks;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class PunchCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.punch.none", "Who do you want to punch?"), client);
            return;
        }

        if (client.getPlayer().getEntity().isRoomMuted() || client.getPlayer().getEntity().getRoom().getRights().hasMute(client.getPlayer().getId())) {
            sendNotif(Locale.getOrDefault("command.user.muted", "You are muted."), client);
            return;
        }

        String punchedPlayer = params[0];
        Session punchedSession = NetworkManager.getInstance().getSessions().getByPlayerUsername(punchedPlayer);

        if (punchedSession == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (punchedSession.getPlayer().getEntity() == null) {
            sendNotif(Locale.getOrDefault("command.user.notinroom", "This user is not in a room."), client);
            return;
        }

        if (punchedSession.getPlayer().getData().getUsername() == client.getPlayer().getData().getUsername()) {
            sendNotif(Locale.getOrDefault("command.punch.himself", "You can't punch yourself!"), client);
            return;
        }

        int posX = punchedSession.getPlayer().getEntity().getPosition().getX();
        int posY = punchedSession.getPlayer().getEntity().getPosition().getY();
        int playerX = client.getPlayer().getEntity().getPosition().getX();
        int playerY = client.getPlayer().getEntity().getPosition().getY();

        if (!((Math.abs((posX - playerX)) >= 2) || (Math.abs(posY - playerY) >= 2))) {
            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), "* " + client.getPlayer().getData().getUsername() + " " + Locale.getOrDefault("command.punch.word", "punched") + " " + punchedSession.getPlayer().getData().getUsername() + " *", 34));
        } else {
            client.getPlayer().getSession().send(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), Locale.getOrDefault("command.notaround", "Oops! %playername% is not near, walk to this player.").replace("%playername%", punchedSession.getPlayer().getData().getUsername()), 34));
            return;
        }
    }

    @Override
    public String getPermission() {
        return "punch_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.punch.description");
    }
}
