package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.game.players.components.types.MessengerFriend;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;

public class LoadFriendsMessageComposer {
    public static Composer compose(Map<Integer, MessengerFriend> friends) {
        Composer msg = new Composer(Composers.LoadFriendsMessageComposer);

        msg.writeInt(1100); // TODO: put this stuff in static config somewhere :P
        msg.writeInt(300);
        msg.writeInt(800);
        msg.writeInt(1100);
        msg.writeInt(0);

        int counter = 0;

        for (Map.Entry<Integer, MessengerFriend> friend : friends.entrySet()) {
            if (friend.getValue() != null && friend.getValue().getData() != null)
                counter++;
        }

        msg.writeInt(counter);

        for (Map.Entry<Integer, MessengerFriend> friend : friends.entrySet()) {
            if (friend.getValue() != null && friend.getValue().getData() != null) {
                friend.getValue().updateClient();
                friend.getValue().serialize(msg);
            }
        }

        return msg;
    }
}
