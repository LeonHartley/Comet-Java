package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.game.groups.types.components.membership.IGroupMember;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.objects.items.types.wall.PostItWallItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class WallItemsMessageComposer extends MessageComposer {
    private final Room room;

    public WallItemsMessageComposer(Room room) {
        this.room = room;
    }

    @Override
    public short getId() {
        return Composers.ItemsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        int size = room.getItems().getWallItems().size();

        if (size > 0) {
            if (room.getGroup() == null) {
                msg.writeInt(1);
                msg.writeInt(room.getData().getOwnerId());
                msg.writeString(room.getData().getOwner());
            } else {
                final IGroup group = room.getGroup();

                if (group.getData().canMembersDecorate()) {
                    msg.writeInt(group.getMembers().getAll().size());

                    for (IGroupMember groupMember : group.getMembers().getAll().values()) {
                        msg.writeInt(groupMember.getPlayerId());
                        msg.writeString(PlayerDao.getUsernameByPlayerId(groupMember.getPlayerId()));
                    }
                } else {
                    msg.writeInt(group.getMembers().getAdministrators().size());

                    for (Integer groupMember : group.getMembers().getAdministrators()) {
                        msg.writeInt(groupMember);
                        msg.writeString(PlayerDao.getUsernameByPlayerId(groupMember));
                    }
                }
            }

        } else {
            msg.writeInt(0);
        }

        msg.writeInt(size);

        for (RoomItemWall item : room.getItems().getWallItems().values()) {
            String extradata = (item instanceof PostItWallItem ? item.getItemData().getData().split(" ")[0] : item.getItemData().getData());
            msg.writeString(item.getVirtualId());
            msg.writeInt(item.getDefinition().getSpriteId());
            msg.writeString(item.getWallPosition());
            msg.writeString(extradata);
            msg.writeInt(0);
            msg.writeInt(room.getData().getOwnerId());
            msg.writeInt(0);
        }
    }
}
