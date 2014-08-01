package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class GroupInformationMessageComposer {
    public static Composer compose(Group group, RoomData roomData, boolean flag, boolean isOwner, boolean isFavourite) {
        Composer msg = new Composer(Composers.GroupInformationMessageComposer);

        msg.writeInt(group.getId());
        msg.writeBoolean(true); //??
        msg.writeInt(group.getData().getType().getTypeId());
        msg.writeString(group.getData().getTitle());
        msg.writeString(group.getData().getDescription());
        msg.writeString(group.getData().getBadge());
        msg.writeInt(roomData.getId());
        msg.writeString(roomData.getName());
        msg.writeInt(1); // Membership (0 = not member, 1 = member, 2 = requested)
        msg.writeInt(group.getMembershipComponent().getMembers().size());
        msg.writeBoolean(false);
        msg.writeString("0/0/90"); // TODO: date created!
        msg.writeBoolean(isOwner);
        msg.writeBoolean(isFavourite);

        msg.writeString(PlayerDao.getUsernameByPlayerId(group.getData().getOwnerId()));

        msg.writeBoolean(flag);
        msg.writeBoolean(flag);

        msg.writeInt(group.getMembershipComponent().getMembershipRequests().size());
        msg.writeBoolean(true);

        return msg;
    }
}
