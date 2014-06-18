package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.game.players.components.types.MessengerFriend;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.messenger.FollowFriendMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class FollowFriendMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int friendId = msg.readInt();

        MessengerFriend friend = client.getPlayer().getMessenger().getFriendById(friendId);

        if (friend == null || friend.updateClient() == null || friend.getClient().getPlayer() == null || friend.getClient().getPlayer().getEntity() == null)
            return;

        Room room = friend.getClient().getPlayer().getEntity().getRoom();

        if (!room.getData().getAccess().equals("open") && room.getData().getOwnerId() != client.getPlayer().getId()) {
            //fuck it yolo
            client.send(HotelViewMessageComposer.compose());
            return;
        }

        client.send(FollowFriendMessageComposer.compose(room.getId()));
    }
}