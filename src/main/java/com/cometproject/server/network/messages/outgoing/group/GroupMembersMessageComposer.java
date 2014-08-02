package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.GroupMember;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.util.Map;

public class GroupMembersMessageComposer {
    public static Composer compose(Group group, int page, int requestType, String searchQuery, boolean isOwner) {
        Composer msg = new Composer(Composers.GroupMembersMessageComposer);

        msg.writeInt(group.getId());
        msg.writeString(group.getData().getTitle());
        msg.writeInt(group.getData().getRoomId());
        msg.writeString(group.getData().getBadge());

        // TODO: Multiple search modes :p
        msg.writeInt(group.getMembershipComponent().getMembers().size());
        msg.writeInt(0);//howmanypages

        for(Map.Entry<Integer, GroupMember> groupMember : group.getMembershipComponent().getMembers().entrySet()) {
            if(groupMember.getKey().equals(group.getData().getOwnerId())) {
                msg.writeInt(2);
            } else {
                // TODO: this
                msg.writeInt(0);
            }

            PlayerData playerData = PlayerDao.getDataById(groupMember.getKey());

            msg.writeString(playerData.getId());
            msg.writeString(playerData.getUsername());
            msg.writeString(playerData.getFigure());
            msg.writeString(""); // TODO: Join date
        }

        msg.writeBoolean(isOwner);
        msg.writeInt(14); // max per page ?
        msg.writeInt(page); // current page

        msg.writeInt(requestType);
        msg.writeString(searchQuery);
        return msg;
    }
}
