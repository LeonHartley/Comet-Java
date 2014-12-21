package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class ModToolBanUserMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
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

        Session user = NetworkManager.getInstance().getSessions().getByPlayerId(userId);
        if (user == null) {
            return;
        }

        if (user == client && user.getPlayer().getPermissions().hasPermission("user_unbannable")) {
            return;
        }

        user.disconnect("banned");

        long expire = Comet.getTime() + (length * 3600);

        if (ipBan) {
            BanManager.getInstance().banPlayer(BanType.IP, user.getIpAddress(), length, expire, message, client.getPlayer().getId());
        }

        if (machineBan) {
            BanManager.getInstance().banPlayer(BanType.MACHINE, user.getUniqueId(), length, expire, message, client.getPlayer().getId());
        }

        BanManager.getInstance().banPlayer(BanType.USER, user.getPlayer().getId() + "", length, expire, message, client.getPlayer().getId());
    }
}
