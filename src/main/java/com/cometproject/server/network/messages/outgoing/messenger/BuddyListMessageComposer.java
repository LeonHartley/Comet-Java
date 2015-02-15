package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.game.players.components.types.messenger.MessengerFriend;
import com.cometproject.server.game.players.data.PlayerAvatar;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.sessions.Session;
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

        msg.writeInt(avatars.size() + (hasStaffChat ? 1 : 0));

        for (PlayerAvatar playerAvatar : avatars) {
            msg.writeInt(playerAvatar.getId());
            msg.writeString(playerAvatar.getUsername());
            msg.writeInt(1);

            boolean isOnline = friends.get(playerAvatar.getId()).isOnline();
            boolean isInRoom = friends.get(playerAvatar.getId()).isInRoom();

            if(friends.get(playerAvatar.getId()).isOnline()) {
                Session playerSession = NetworkManager.getInstance().getSessions().getByPlayerId(playerAvatar.getId());

                if(playerSession.getPlayer() != null) {
                    if(playerSession.getPlayer().getSettings().getHideInRoom()) {
                        isInRoom = false;
                    }

                    if(playerSession.getPlayer().getSettings().getHideOnline()) {
                        isOnline = false;
                    }
                }
            }

            msg.writeBoolean(isOnline);
            msg.writeBoolean(isInRoom);

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

        if(hasStaffChat) {
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
            msg.writeShort(0);
        }

        avatars.clear();
        return msg;
    }
}
