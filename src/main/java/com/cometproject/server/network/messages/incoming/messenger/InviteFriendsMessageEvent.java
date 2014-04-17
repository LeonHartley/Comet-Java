package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.messenger.InviteFriendMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastList;

import java.util.List;

public class InviteFriendsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int friendCount = msg.readInt();
        List<Integer> friends = new FastList<>();

        for (int i = 0; i < friendCount; i++) {
            friends.add(msg.readInt());
        }

        String message = msg.readString();

        client.getPlayer().getMessenger().broadcast(friends, InviteFriendMessageComposer.compose(client.getPlayer().getId(), message));
    }
}
