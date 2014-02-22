package com.cometproject.network.messages.outgoing.messenger;

import com.cometproject.game.players.components.types.MessengerFriend;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

import java.util.Map;

public class LoadFriendsMessageComposer {
    public static Composer compose(Map<Integer, MessengerFriend> friends) {
        Composer msg = new Composer(Composers.LoadFriendsMessageComposer);

        msg.writeInt(1100);
        msg.writeInt(300);
        msg.writeInt(800);
        msg.writeInt(1100);
        msg.writeInt(0);

        msg.writeInt(friends.size());

        for(Map.Entry<Integer, MessengerFriend> friend : friends.entrySet()) {
            friend.getValue().updateClient();
            friend.getValue().serialize(msg);
        }

        return msg;
    }
}
