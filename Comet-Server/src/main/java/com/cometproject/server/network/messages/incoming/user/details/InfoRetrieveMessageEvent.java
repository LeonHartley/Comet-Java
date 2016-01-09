package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.messenger.MessengerConfigMessageComposer;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorMetaDataMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.achievements.AchievementPointsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.achievements.AchievementRequirementsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.buildersclub.BuildersClubMembershipMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.UserObjectMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.BadgeInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.permissions.AllowancesMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.permissions.CitizenshipStatusMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class InfoRetrieveMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        client.getPlayer().sendBalance();

        // TODO: Queue these & send all at once.
        client.send(new UserObjectMessageComposer(client.getPlayer()));
        client.send(new BuildersClubMembershipMessageComposer());
        client.send(new AllowancesMessageComposer(client.getPlayer().getData().getRank()));
//        client.send(new CitizenshipStatusMessageComposer());
        client.send(new AchievementPointsMessageComposer(client.getPlayer().getData().getAchievementPoints()));

        client.send(new MessengerConfigMessageComposer());

        client.send(new BadgeInventoryMessageComposer(client.getPlayer().getInventory().getBadges()));
        client.send(new AchievementRequirementsMessageComposer());

        client.getPlayer().getMessenger().sendStatus(true, client.getPlayer().getEntity() != null);
    }
}
