package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolUserInfoMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.sql.ResultSet;

public class ModToolUserInfoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();

        if (!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.getLogger().error(
                    ModToolUserInfoMessageEvent.class.getName() + " - tried to gather information on user: " + userId);
            return;
        }

        try {
            ResultSet user = Comet.getServer().getStorage().getRow("SELECT * FROM players WHERE id = " + userId);

            if (user == null) {
                return;
            }

            ResultSet stats = Comet.getServer().getStorage().getRow("SELECT * FROM player_stats WHERE player_id = " + userId);

            if (stats == null) {
                return;
            }

            client.send(ModToolUserInfoMessageComposer.compose(user, stats));
        } catch (Exception e) {
            GameEngine.getLogger().error("Error while sending user info to mod tool", e);
        }
    }
}
