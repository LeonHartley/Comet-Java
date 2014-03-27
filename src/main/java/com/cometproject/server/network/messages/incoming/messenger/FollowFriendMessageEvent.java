package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.game.players.components.types.MessengerFriend;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.messenger.FollowFriendMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomDataMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class FollowFriendMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int friendId = msg.readInt();

        MessengerFriend friend = client.getPlayer().getMessenger().getFriendById(friendId);

        if(friend == null || friend.getClient() == null || friend.getClient().getPlayer() == null || friend.getClient().getPlayer().getEntity() == null)
            return;

        Room room = friend.getClient().getPlayer().getEntity().getRoom();

        client.send(FollowFriendMessageComposer.compose(room.getId()));
        client.send(RoomDataMessageComposer.compose(room));
    }
}