package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.IntArrayItemData;
import com.cometproject.api.game.rooms.objects.data.ItemData;
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
    public ItemData createItemData() {
        final BackgroundTonerData data = BackgroundTonerData.get(this.getItemData().getData());
        final boolean enabled = (data != null);

        if (!enabled) {
            this.getItemData().setData("0;#;0;#;0");
            this.saveData();
        }

        return new IntArrayItemData(new int[] {
                enabled ? 1 : 0,
                enabled ? data.getHue() : 0,
                enabled ? data.getSaturation() : 0,
                enabled ? data.getLightness() : 0,
        });
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        this.getItemData().setData("");
        this.saveData();
        this.getRoom().getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(this));

        return true;
    }
}
