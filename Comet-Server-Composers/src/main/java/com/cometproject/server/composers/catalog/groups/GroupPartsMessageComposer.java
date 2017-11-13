package com.cometproject.server.composers.catalog.groups;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.List;


public class GroupPartsMessageComposer extends MessageComposer {

    private final List<IRoomData> availableRooms;

    public GroupPartsMessageComposer(final List<IRoomData> rooms) {
        this.availableRooms = rooms;
    }

    @Override
    public short getId() {
        return Composers.GroupCreationWindowMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(CometSettings.groupCost);
        msg.writeInt(availableRooms.size());

        for (IRoomData roomData : availableRooms) {
            msg.writeInt(roomData.getId());
            msg.writeString(roomData.getName());
            msg.writeBoolean(false);
        }


        // TODO: Stop hardcoding this
        msg.writeInt(5);
        msg.writeInt(10);
        msg.writeInt(3);
        msg.writeInt(4);
        msg.writeInt(0x19);
        msg.writeInt(0x11);
        msg.writeInt(5);
        msg.writeInt(0x19);
        msg.writeInt(0x11);
        msg.writeInt(3);
        msg.writeInt(0x1d);
        msg.writeInt(11);
        msg.writeInt(4);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);
    }

    @Override
    public void dispose() {
        this.availableRooms.clear();
    }
}
