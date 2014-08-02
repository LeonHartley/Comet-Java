package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.outgoing.user.details.UserInfoMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GroupInformationMessageComposer {
    public static Composer compose(Group group, RoomData roomData, boolean flag, boolean isOwner, boolean isFavourite, int membership) {
        Composer msg = new Composer(Composers.GroupInformationMessageComposer);

        msg.writeInt(group.getId());
        msg.writeBoolean(true); //??
        msg.writeInt(group.getData().getType().getTypeId());
        msg.writeString(group.getData().getTitle());
        msg.writeString(group.getData().getDescription());
        msg.writeString(group.getData().getBadge());
        msg.writeInt(roomData.getId());
        msg.writeString(roomData.getName());
        msg.writeInt(membership);
        msg.writeInt(group.getMembershipComponent().getMembers().size());
        msg.writeBoolean(false);
        msg.writeString(getDate(group.getData().getCreatedTimestamp()));
        msg.writeBoolean(isOwner);
        msg.writeBoolean(isFavourite);

        msg.writeString(PlayerDao.getUsernameByPlayerId(group.getData().getOwnerId()));

        msg.writeBoolean(flag);
        msg.writeBoolean(flag);

        msg.writeInt(group.getMembershipComponent().getMembershipRequests().size());
        msg.writeBoolean(true);

        return msg;
    }

    public static String getDate(int timestamp) {
        Date d = new Date(timestamp * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        return df.format(d);
    }
}
