package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.messenger.InviteFriendMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;
import java.util.List;


public class InviteFriendsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        final long time = System.currentTimeMillis();

        if (!client.getPlayer().getPermissions().hasPermission("bypass_flood")) {
            if (time - client.getPlayer().getMessengerLastMessageTime() < 750) {
                client.getPlayer().setMessengerFloodFlag(client.getPlayer().getMessengerFloodFlag() + 1);

                if (client.getPlayer().getMessengerFloodFlag() >= 4) {
                    client.getPlayer().setMessengerFloodTime(30);
                    client.getPlayer().setMessengerFloodFlag(0);

                }
            }

            if (client.getPlayer().getMessengerFloodTime() >= 1) {
                return;
            }

            client.getPlayer().setMessengerLastMessageTime(time);
        }


        int friendCount = msg.readInt();
        List<Integer> friends = new ArrayList<>();

        for (int i = 0; i < friendCount; i++) {
            friends.add(msg.readInt());
        }

        String message = msg.readString();

        client.getPlayer().getMessenger().broadcast(friends, new InviteFriendMessageComposer(message, client.getPlayer().getId()));
        friends.clear();
    }
}
