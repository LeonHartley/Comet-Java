package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.types.MessengerFriend;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.messenger.InstantChatMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.FloodFilterMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class PrivateChatMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();
        String message = msg.readString();

        if (userId == -1) {
            for(Session user : Comet.getServer().getNetwork().getSessions().getbyPlayerPermission("staff_chat")) {
                if(user == client) continue;
                user.send(InstantChatMessageComposer.compose(client.getPlayer().getData().getUsername() + ": " + message, -1));
            }
            return;
        }

        MessengerFriend friend = client.getPlayer().getMessenger().getFriendById(userId);

        if (friend == null) {
            return;
        }

        friend.updateClient();
        Session friendClient = friend.getClient();

        if (friendClient == null) {
            return;
        }

        long time = System.currentTimeMillis();

        if (time - client.getPlayer().lastMessage < 750) { // TODO: add flood bypass for staff with permission or something
            client.getPlayer().floodFlag++;

            if (client.getPlayer().floodFlag >= 4) {
                client.getPlayer().floodTime = 30;
                client.getPlayer().floodFlag = 0;

                client.send(FloodFilterMessageComposer.compose(client.getPlayer().floodTime));
            }
        }

        if (client.getPlayer().floodTime >= 1) {
            return;
        }

        client.getPlayer().lastMessage = time;


        friendClient.send(InstantChatMessageComposer.compose(message, client.getPlayer().getId()));
    }
}
