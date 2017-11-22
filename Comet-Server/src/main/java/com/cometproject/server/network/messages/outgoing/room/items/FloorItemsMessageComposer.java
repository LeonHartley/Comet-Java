package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.Map;


public class FloorItemsMessageComposer extends MessageComposer {
    private final Room room;

    public FloorItemsMessageComposer(final Room room) {
        this.room = room;
    }

    @Override
    public short getId() {
        return Composers.ObjectsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        if (room.getItems().getFloorItems().size() > 0) {
            //if (room.getGroup() == null) {
            msg.writeInt(room.getItems().getItemOwners().size());

            for (Map.Entry<Integer, String> itemOwner : room.getItems().getItemOwners().entrySet()) {
                msg.writeInt(itemOwner.getKey());
                msg.writeString(itemOwner.getValue());
            }
            ;
           /* } else {
                final Group group = room.getGroup();

                if (group.getData().canMembersDecorate()) {
                    msg.writeInt(group.getMembershipComponent().getMembers().size() + 1);

                    msg.writeInt(room.getData().getOwnerId());
                    msg.writeString(room.getData().getOwner());

                    for (GroupMember groupMember : group.getMembershipComponent().getMembers().values()) {
                        msg.writeInt(groupMember.getPlayerId());
                        msg.writeString(PlayerDao.getUsernameByPlayerId(groupMember.getPlayerId()));
                    }
                } else {
                    msg.writeInt(group.getMembershipComponent().getAdministrators().size() + 1);

                    msg.writeInt(room.getData().getOwnerId());
                    msg.writeString(room.getData().getOwner());

                    for (Integer groupMember : group.getMembershipComponent().getAdministrators()) {
                        msg.writeInt(groupMember);
                        msg.writeString(PlayerDao.getUsernameByPlayerId(groupMember));
                    }
                }
            }*/

            msg.writeInt(room.getItems().getFloorItems().size());

            for (RoomItemFloor item : room.getItems().getFloorItems().values()) {
                item.serialize((msg));
            }
        } else {
            msg.writeInt(0);
            msg.writeInt(0);
        }

    }
}
