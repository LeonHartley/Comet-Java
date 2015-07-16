package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.messenger.InviteFriendMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;
import java.util.List;


public class InviteFriendsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
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

        if (!client.getPlayer().getPermissions().hasPermission("bypass_filter")) {
            FilterResult filterResult = RoomManager.getInstance().getFilter().filter(message);

            if (filterResult.isBlocked()) {
                client.send(new AdvancedAlertMessageComposer(Locale.get("game.message.blocked").replace("%s", filterResult.getMessage())));
                return;
            } else if (filterResult.wasModified()) {
                message = filterResult.getMessage();
            }
        }

        client.getPlayer().getMessenger().broadcast(friends, new InviteFriendMessageComposer(message, client.getPlayer().getId()));
        friends.clear();
    }
}
