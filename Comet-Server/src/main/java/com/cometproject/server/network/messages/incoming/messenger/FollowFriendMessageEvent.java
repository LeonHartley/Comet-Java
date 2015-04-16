package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.game.players.components.types.messenger.MessengerFriend;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class FollowFriendMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int friendId = msg.readInt();

        MessengerFriend friend = client.getPlayer().getMessenger().getFriendById(friendId);

        if (friend == null || !friend.isInRoom())
            return;

        RoomInstance room = friend.getSession().getPlayer().getEntity().getRoom();

        if (room == null) {
            // wtf?
            return;
        }

        client.send(new RoomForwardMessageComposer(room.getId()));
    }
}