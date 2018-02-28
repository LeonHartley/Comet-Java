package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.data.BackgroundTonerData;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;


public class BackgroundTonerFloorItem extends RoomItemFloor {
    public BackgroundTonerFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void composeItemData(IComposer msg) {
        BackgroundTonerData data = BackgroundTonerData.get(this.getItemData().getData());

        boolean enabled = (data != null);

        msg.writeInt(0);
        msg.writeInt(5);
        msg.writeInt(4);
        msg.writeInt(enabled ? 1 : 0);

        if (enabled) {
            msg.writeInt(data.getHue());
            msg.writeInt(data.getSaturation());
            msg.writeInt(data.getLightness());
        } else {
            this.getItemData().setData("0;#;0;#;0");
            this.saveData();

            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
        }
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        this.getItemData().setData("");
        this.saveData();
        this.getRoom().getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(this));

        return true;
    }
}
