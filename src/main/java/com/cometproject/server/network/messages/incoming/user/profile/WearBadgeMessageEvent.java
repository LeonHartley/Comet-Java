package com.cometproject.server.network.messages.incoming.user.profile;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.profile.UserBadgesMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.sql.PreparedStatement;
import java.util.Map;

public class WearBadgeMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        client.getPlayer().getInventory().resetBadgeSlots();

        for(int i = 0; i < 5; i++) {
            int slot = msg.readInt();
            String badge = msg.readString();

            if(badge.isEmpty()) {
                continue;
            }

            if(!client.getPlayer().getInventory().getBadges().containsKey(badge) || slot < 1 || slot > 5) {
                return;
            }

            client.getPlayer().getInventory().getBadges().replace(badge, slot);
        }

        for(Map.Entry<String, Integer> badgeToUpdate : client.getPlayer().getInventory().getBadges().entrySet()) {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE player_badges SET slot = ? WHERE badge_code = ? AND player_id = ?");

            statement.setInt(1, badgeToUpdate.getValue());
            statement.setString(2, badgeToUpdate.getKey());
            statement.setInt(3, client.getPlayer().getId());

            statement.executeUpdate();
        }

        if(client.getPlayer().getEntity() != null) {
            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(UserBadgesMessageComposer.compose(client.getPlayer().getId(), client.getPlayer().getInventory().equippedBadges()));
        } else {
            client.send(UserBadgesMessageComposer.compose(client.getPlayer().getId(), client.getPlayer().getInventory().equippedBadges()));
        }
    }
}
