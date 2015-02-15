package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.game.players.components.types.messenger.MessengerFriend;
import com.cometproject.server.game.players.data.PlayerAvatar;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class BuddyListMessageComposer {
    public static Composer compose(Map<Integer, MessengerFriend> friends, boolean hasStaffChat) {
        Composer msg = new Composer(Composers.BuddyListMessageComposer);

        msg.writeInt(0);///??
        msg.writeInt(0);///??

        List<PlayerAvatar> avatars = Lists.newArrayList();

        for (Map.Entry<Integer, MessengerFriend> friend : friends.entrySet()) {
            if (friend.getValue() != null) {
                final PlayerAvatar playerAvatar = friend.getValue().getAvatar();

                if(playerAvatar != null) {
                    avatars.add(playerAvatar);
                }
            }
        }

        msg.writeInt(avatars.size());

        for (PlayerAvatar playerAvatar : avatars) {
            msg.writeInt(playerAvatar.getId());
            msg.writeString(playerAvatar.getUsername());
            msg.writeInt(1);
            msg.writeBoolean(friends.get(playerAvatar.getId()).isOnline());
            msg.writeBoolean(friends.get(playerAvatar.getId()).isInRoom());
            msg.writeString(playerAvatar.getFigure());
            msg.writeInt(0);
            msg.writeString(playerAvatar.getMotto());
            msg.writeString("");
            msg.writeString("");
            msg.writeBoolean(false);
            msg.writeBoolean(false);
            msg.writeBoolean(false);
            msg.writeShort(0);
        }


        avatars.clear();
        return msg;
    }
}
