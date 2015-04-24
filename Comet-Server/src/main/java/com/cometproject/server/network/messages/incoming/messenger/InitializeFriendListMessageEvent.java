package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.messenger.BuddyListMessageComposer;
import com.cometproject.server.network.messages.outgoing.messenger.FriendRequestsMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class InitializeFriendListMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.send(new BuddyListMessageComposer(client.getPlayer().getMessenger().getFriends(), client.getPlayer().getPermissions().hasPermission("staff_chat")));
        client.send(new FriendRequestsMessageComposer(client.getPlayer().getMessenger().getRequestAvatars()));
    }
}
