package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.ws.messages.piano.ClosePianoMessage;
import com.cometproject.server.network.ws.messages.piano.OpenPianoMessage;

public class PianoFloorItem extends DefaultFloorItem {
    public PianoFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onToggled(PlayerEntity playerEntity) {
        if (this.getItemData().getData().equals("0")) {
            this.getRoom().getEntities().broadcastWs(new ClosePianoMessage());
        } else if (this.getItemData().getData().equals("1")) {
            this.getRoom().getEntities().broadcastWs(new OpenPianoMessage());
        }
    }
}
