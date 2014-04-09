package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.game.players.components.types.MessengerFriend;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class DeleteFriendMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int friendCount = msg.readInt();

        for(int i = 0; i < friendCount; i++) {
            int userId = msg.readInt();

            MessengerFriend friend = client.getPlayer().getMessenger().getFriendById(userId);

            if(friend == null)
                continue;

            Session friendClient = friend.updateClient();

            if(friendClient != null && friendClient.getPlayer() != null) {
                friendClient.getPlayer().getMessenger().removeFriend(client.getPlayer().getId());
            }

            client.getPlayer().getMessenger().removeFriend(userId);
        }
    }
}
