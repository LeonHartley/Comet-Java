package com.cometsrv.network.messages.incoming.messenger;

import com.cometsrv.game.players.components.types.MessengerFriend;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.messenger.InstantChatMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class PrivateChatMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();
        String message = msg.readString();

        if(userId == -1) { // TODO: Staff chat!

            return;
        }

        MessengerFriend friend = client.getPlayer().getMessenger().getFriendById(userId);

        if(friend == null) {
            return;
        }

        friend.updateClient();
        Session friendClient = friend.getClient();

        if(friendClient == null) {
            return;
        }

        friendClient.send(InstantChatMessageComposer.compose(message, client.getPlayer().getId()));
    }
}
