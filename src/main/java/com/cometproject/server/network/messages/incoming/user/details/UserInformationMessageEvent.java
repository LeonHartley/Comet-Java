package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.messenger.FriendRequestsMessageComposer;
import com.cometproject.server.network.messages.outgoing.messenger.LoadFriendsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.achievements.AchievementPointsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.UserObjectMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.WelcomeUserMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.BadgeInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.permissions.AllowancesMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class UserInformationMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.getPlayer().sendBalance();

        client.send(UserObjectMessageComposer.compose(client.getPlayer()));

//        client.send(WelcomeUserMessageComposer.compose());
        client.send(AllowancesMessageComposer.compose(client.getPlayer().getData().getRank()));

        client.send(AchievementPointsMessageComposer.compose(client.getPlayer().getData().getAchievementPoints()));

        client.send(LoadFriendsMessageComposer.compose(client.getPlayer().getMessenger().getFriends(), client.getPlayer().getPermissions().hasPermission("staff_chat")));
        client.send(FriendRequestsMessageComposer.compose(client.getPlayer().getMessenger().getRequests()));

        client.send(BadgeInventoryMessageComposer.compose(client.getPlayer().getInventory().getBadges()));

        client.getPlayer().getMessenger().sendStatus(true, client.getPlayer().getEntity() != null);
    }
}
