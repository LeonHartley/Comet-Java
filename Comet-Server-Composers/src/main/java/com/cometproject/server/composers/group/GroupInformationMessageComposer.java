package com.cometproject.server.composers.group;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.text.SimpleDateFormat;
import java.util.Date;


public class GroupInformationMessageComposer extends MessageComposer {
    private final IGroup group;
    private final IRoomData roomData;
    private final boolean flag;
    private final boolean isOwner;
    private final boolean isAdmin;
    private final int membership;

    public GroupInformationMessageComposer(final IGroup group, final IRoomData roomData, final boolean flag, final boolean isOwner, final boolean isAdmin, final int membership) {
        this.group = group;
        this.roomData = roomData;
        this.flag = flag;
        this.isOwner = isOwner;
        this.isAdmin = isAdmin;
        this.membership = membership;
    }

    @Override
    public short getId() {
        return Composers.GroupInfoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(group.getId());
        msg.writeBoolean(true); //is visible
        msg.writeInt(group.getData().getType().getTypeId());
        msg.writeString(group.getData().getTitle());
        msg.writeString(group.getData().getDescription());
        msg.writeString(group.getData().getBadge());
        msg.writeInt(roomData == null ? 0 : roomData.getId());
        msg.writeString(roomData == null ? "Unknown Room" : roomData.getName());
        msg.writeInt(membership);
        msg.writeInt(group.getMembers().getAll().size());
        msg.writeBoolean(false);
        msg.writeString(getDate(group.getData().getCreatedTimestamp()));
        msg.writeBoolean(isOwner);
        msg.writeBoolean(isAdmin);

        msg.writeString(group.getData().getOwnerName());

        msg.writeBoolean(flag);
        msg.writeBoolean(group.getData().canMembersDecorate());

        msg.writeInt((isOwner || isAdmin) ? group.getMembers().getMembershipRequests().size() : 0);
        msg.writeBoolean(group.getData().hasForum());
    }

    public static String getDate(int timestamp) {
        Date d = new Date(timestamp * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        return df.format(d);
    }
}
