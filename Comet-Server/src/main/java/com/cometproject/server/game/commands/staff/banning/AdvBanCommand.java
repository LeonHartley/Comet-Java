package com.cometproject.server.game.commands.staff.banning;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;

import java.util.List;


public class AdvBanCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            sendNotif(Locale.getOrDefault("command.params.length", "Oops! You did something wrong!"), client);
            return;
        }

        String username = params[0];

        Session user = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (user == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (user == client || !user.getPlayer().getPermissions().getRank().bannable() || user.getPlayer().getPermissions().getRank().getId() >= client.getPlayer().getPermissions().getRank().getId()) {
            sendNotif(Locale.getOrDefault("command.user.notbannable", "You're not able to ban this user!"), client);
            return;
        }

        long expire = Comet.getTime() + (72 * 3600);

        String ipAddress = user.getIpAddress();

        if (!BanManager.getInstance().hasBan(ipAddress, BanType.IP)) {
            BanManager.getInstance().banPlayer(BanType.IP, user.getIpAddress(), 72, expire, Locale.getOrDefault("command.advban.reason", "Advertising"), client.getPlayer().getId());
        }

        BanManager.getInstance().banPlayer(BanType.USER, user.getPlayer().getId() + "", 72, expire, Locale.getOrDefault("command.advban.reason", "Advertising"), client.getPlayer().getId());

        List<Integer> playerIds = PlayerManager.getInstance().getPlayerIdsByIpAddress(ipAddress);

        for (int playerId : playerIds) {
            Session player = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

            if (player != null) {
                player.disconnect("banned");
            }
        }

        playerIds.clear();

        this.logDesc = "El staff %s ha hecho advBan en la sala '%b' al usuario %u"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", client.getPlayer().getEntity().getRoom().getData().getName())
                .replace("%u", username);
    }

    @Override
    public String getPermission() {
        return "advban_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.advban", "%username% %reason%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.advban.description");
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
