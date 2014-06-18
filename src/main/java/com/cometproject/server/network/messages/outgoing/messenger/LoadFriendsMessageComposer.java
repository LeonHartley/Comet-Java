package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.game.players.components.types.MessengerFriend;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;

public class LoadFriendsMessageComposer {
    public static Composer compose(Map<Integer, MessengerFriend> friends, boolean hasStaffChat) {
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

        msg.writeInt(counter + (hasStaffChat ? 1 : 0));

        for (Map.Entry<Integer, MessengerFriend> friend : friends.entrySet()) {
            if (friend.getValue() != null && friend.getValue().getData() != null) {
                friend.getValue().updateClient();
                friend.getValue().serialize(msg);
            }
        }

        if (hasStaffChat) {
            msg.writeInt(-1);
            msg.writeString("Staff chat");
            msg.writeInt(1);
            msg.writeBoolean(true);
            msg.writeBoolean(false);
            msg.writeString("hr-831-45.fa-1206-91.sh-290-1331.ha-3129-100.hd-180-2.cc-3039-73.ch-3215-92.lg-270-73");
            msg.writeInt(0);
            msg.writeString("");
            msg.writeString("");
            msg.writeString("");
            msg.writeBoolean(true);
            msg.writeBoolean(true);
            msg.writeBoolean(false);
            msg.writeBoolean(false);
            msg.writeBoolean(false);
        }

        return msg;
    }
}
