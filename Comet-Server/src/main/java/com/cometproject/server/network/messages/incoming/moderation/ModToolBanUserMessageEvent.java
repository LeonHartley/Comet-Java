package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.permissions.types.Permission;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class ModToolBanUserMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int userId = msg.readInt();
        String message = msg.readString();
        int length = msg.readInt();
        String category = msg.readString();
        String presetAction = msg.readString();

        if (!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.disconnect();
            return;
        }

        boolean ipBan = msg.readBoolean();
        boolean machineBan = msg.readBoolean();

        long expire = Comet.getTime() + (length * 3600);

        Session user = NetworkManager.getInstance().getSessions().getByPlayerId(userId);

        if (user == null) {
            PlayerData playerData = PlayerDao.getDataById(userId);

            if(playerData == null) {
                return;
            }

            if(PermissionsManager.getInstance().getPermissions().get("user_unbannable") != null) {
                final Permission permission = PermissionsManager.getInstance().getPermissions().get("user_unbannable");

                if(permission.getRank() <= playerData.getRank()) {
                    client.send(new AlertMessageComposer(Locale.getOrDefault("mod.ban.nopermission", "You do not have permission to ban this player!")));
                    return;
                }
            }

            this.banPlayer(ipBan, machineBan, expire, userId, client.getPlayer().getId(), length, message, playerData.getIpAddress(), "");
            return;
        }

        if (user == client) {
            return;
        }

        if(user.getPlayer().getPermissions().hasPermission("user_unbannable")) {
            client.send(new AlertMessageComposer(Locale.getOrDefault("mod.ban.nopermission", "You do not have permission to ban this player!")));
            return;
        }

        user.disconnect("banned");

        this.banPlayer(ipBan, machineBan, expire, user.getPlayer().getId(), client.getPlayer().getId(), length, message, user.getIpAddress(), machineBan ? user.getUniqueId() : "");
    }

    private void banPlayer(boolean ipBan, boolean machineBan, long expire, int playerId, int moderatorId, int length, String message, String ipAddress, String uniqueId) {
        this.updateStats(playerId);

        if (ipBan) {
            BanManager.getInstance().banPlayer(BanType.IP, ipAddress, length, expire, message, moderatorId);
        }

        if (machineBan && !uniqueId.isEmpty()) {
            BanManager.getInstance().banPlayer(BanType.MACHINE, uniqueId, length, expire, message, moderatorId);
        }

        BanManager.getInstance().banPlayer(BanType.USER, playerId + "", length, expire, message, moderatorId);
    }

    private void updateStats(int playerId) {
        PlayerStatistics playerStatistics = PlayerDao.getStatisticsById(playerId);

        if(playerStatistics != null) {
            playerStatistics.incrementBans(1);
        }
    }
}
