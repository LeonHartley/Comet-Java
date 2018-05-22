package com.cometproject.server.network.messages.incoming.user.profile;

import com.cometproject.api.game.quests.QuestType;
import com.cometproject.api.utilities.Pair;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.profile.UserBadgesMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;
import com.cometproject.storage.mysql.queues.players.objects.PlayerBadgeUpdate;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;


public class WearBadgeMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.getPlayer().getInventory().resetBadgeSlots();


        final long currentTimeMs = System.currentTimeMillis();
        final long timeSinceLastUpdate = currentTimeMs - client.getPlayer().getLastBadgeUpdate();

        if (timeSinceLastUpdate < 350) {
            return;
        }

        for (int i = 0; i < 5; i++) {
            final int slot = msg.readInt();
            final String badge = msg.readString();

            if (badge.isEmpty()) {
                continue;
            }

            if (!client.getPlayer().getInventory().getBadges().containsKey(badge) || slot < 1 || slot > 5) {
                return;
            }

            client.getPlayer().getInventory().getBadges().replace(badge, slot);
        }

        //final Set<Pair<Integer, PlayerBadgeUpdate>> updates = Sets.newHashSet();

        final int playerId = client.getPlayer().getId();

        for (Map.Entry<String, Integer> badgeToUpdate : client.getPlayer().getInventory().getBadges().entrySet()) {
            //updates.add(new Pair(playerId, new PlayerBadgeUpdate(playerId, badgeToUpdate.getKey(), badgeToUpdate.getValue())));
            InventoryDao.updateBadge(badgeToUpdate.getKey(), badgeToUpdate.getValue(), client.getPlayer().getId());
        }

//        MySQLStorageQueues.instance().getPlayerBadgeUpdateQueue().addAll(updates);

        if (client.getPlayer().getEntity() != null) {
            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UserBadgesMessageComposer(client.getPlayer().getId(), client.getPlayer().getInventory().equippedBadges()));
        } else {
            client.send(new UserBadgesMessageComposer(client.getPlayer().getId(), client.getPlayer().getInventory().equippedBadges()));
        }

        client.getPlayer().getQuests().progressQuest(QuestType.PROFILE_BADGE);
        client.getPlayer().setLastBadgeUpdate(currentTimeMs);
    }
}
