package com.cometproject.server.network.messages.incoming.user.profile;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.profile.UserBadgesMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;

import java.util.Map;


public class WearBadgeMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.getPlayer().getInventory().resetBadgeSlots();

        for (int i = 0; i < 5; i++) {
            int slot = msg.readInt();
            String badge = msg.readString();

            if (badge.isEmpty()) {
                continue;
            }

            if (!client.getPlayer().getInventory().getBadges().containsKey(badge) || slot < 1 || slot > 5) {
                return;
            }

            client.getPlayer().getInventory().getBadges().replace(badge, slot);
        }

        for (Map.Entry<String, Integer> badgeToUpdate : client.getPlayer().getInventory().getBadges().entrySet()) {
            InventoryDao.updateBadge(badgeToUpdate.getKey(), badgeToUpdate.getValue(), client.getPlayer().getId());
        }

        if (client.getPlayer().getEntity() != null) {
            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UserBadgesMessageComposer(client.getPlayer().getId(), client.getPlayer().getInventory().equippedBadges()));
        } else {
            client.send(new UserBadgesMessageComposer(client.getPlayer().getId(), client.getPlayer().getInventory().equippedBadges()));
        }
    }
}
