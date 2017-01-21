package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.players.components.types.messenger.MessengerFriend;
import com.cometproject.server.game.players.data.PlayerAvatar;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class BuddyListMessageComposer extends MessageComposer {
    private final Map<Integer, MessengerFriend> friends;
    private final List<PlayerAvatar> avatars;
    private final List<Integer> groups;

    private final boolean hasStaffChat;

    public BuddyListMessageComposer(Map<Integer, MessengerFriend> friends, final boolean hasStaffChat, final List<Integer> groups) {
        this.hasStaffChat = hasStaffChat;

        this.friends = friends;
        this.avatars = Lists.newArrayList();

        for (Map.Entry<Integer, MessengerFriend> friend : friends.entrySet()) {
            if (friend.getValue() != null) {
                final PlayerAvatar playerAvatar = friend.getValue().getAvatar();

                if (playerAvatar != null) {
                    avatars.add(playerAvatar);
                }
            }
        }

        this.groups = groups;
    }

    @Override
    public short getId() {
        return Composers.BuddyListMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(1);//?
        msg.writeInt(0);//?

        if (CometSettings.groupChatEnabled) {
            msg.writeInt(avatars.size() + (hasStaffChat ? 1 : 0) + this.groups.size());
        } else {
            msg.writeInt(avatars.size() + (hasStaffChat ? 1 : 0));
        }

        for (PlayerAvatar playerAvatar : avatars) {
            msg.writeInt(playerAvatar.getId());
            msg.writeString(playerAvatar.getUsername());
            msg.writeInt(77); // Male.

            boolean isOnline = friends.get(playerAvatar.getId()).isOnline();
            boolean isInRoom = friends.get(playerAvatar.getId()).isInRoom();

            if (friends.get(playerAvatar.getId()).isOnline()) {
                Session playerSession = NetworkManager.getInstance().getSessions().getByPlayerId(playerAvatar.getId());

                if (playerSession != null && playerSession.getPlayer() != null) {
                    if (playerSession.getPlayer().getSettings().getHideInRoom()) {
                        isInRoom = false;
                    }

                    if (playerSession.getPlayer().getSettings().getHideOnline()) {
                        isOnline = false;
                    }
                }
            }

            msg.writeBoolean(isOnline);
            msg.writeBoolean(isInRoom);

            msg.writeString(playerAvatar.getFigure());
            msg.writeInt(0); // category id
            msg.writeString(playerAvatar.getMotto());
            msg.writeString("");
            msg.writeString("");
            msg.writeBoolean(false);
            msg.writeBoolean(false);
            msg.writeBoolean(false);
            msg.writeShort(0);
        }

        if (hasStaffChat) {
            msg.writeInt(Integer.MAX_VALUE);
            msg.writeString("Staff chat");
            msg.writeInt(77);
            msg.writeBoolean(true);
            msg.writeBoolean(false);
            msg.writeString("hr-831-45.fa-1206-91.sh-290-1331.ha-3129-100.hd-180-2.cc-3039-73.ch-3215-92.lg-270-73");
            msg.writeInt(0);
            msg.writeString("");
            msg.writeString("");
            msg.writeString("");
            msg.writeBoolean(false);
            msg.writeBoolean(false);
            msg.writeBoolean(false);
            msg.writeShort(0);
        }

        if (CometSettings.groupChatEnabled) {
            for (Integer groupId : this.groups) {
                final Group group = GroupManager.getInstance().get(groupId);

                msg.writeInt(-groupId);
                msg.writeString(group.getData().getTitle());
                msg.writeInt(0);
                msg.writeBoolean(true);
                msg.writeBoolean(false);
                msg.writeString(group.getData().getBadge());
                msg.writeInt(1);
                msg.writeString("");
                msg.writeString("");
                msg.writeString("");
                msg.writeBoolean(false);
                msg.writeBoolean(false);
                msg.writeBoolean(false);
                msg.writeShort(0);

            }
        }
    }

    @Override
    public void dispose() {
        this.avatars.clear();
    }
}