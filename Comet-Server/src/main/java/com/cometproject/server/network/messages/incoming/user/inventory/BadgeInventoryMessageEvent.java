package com.cometproject.server.network.messages.incoming.user.inventory;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.inventory.BadgeInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.profile.UserBadgesMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;


public class BadgeInventoryMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int userId = msg.readInt();

        if (userId == client.getPlayer().getId()) {
            client.send(new BadgeInventoryMessageComposer(client.getPlayer().getInventory().getBadges()));
            return;
        }

        client.send(new UserBadgesMessageComposer(userId, InventoryDao.getWornBadgesByPlayerId(userId)));
    }
}
