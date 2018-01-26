package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;


import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class CrackableFloorItem extends RoomItemFloor {

    public CrackableFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);

        this.getItemData().setData("0");
    }

    @Override
    public boolean onInteract(RoomEntity entity, int state, boolean isWiredTrigger) {
        int hits = Integer.parseInt(this.getItemData().getData());
        int maxHits = 20;

        if (hits <= maxHits) {
            hits++;
        } else {
            // we're open!
        }

        this.getItemData().setData(hits);
        this.sendUpdate();

        return true;
    }

    @Override
    public void composeItemData(IComposer msg) {
        msg.writeInt(0);
        msg.writeInt(7);

        int state = Integer.parseInt(this.getItemData().getData());

        msg.writeString(this.calculateState(20, state));
        msg.writeInt(state);//state
        msg.writeInt(20);//max
    }

    private int calculateState(int maxHits, int currentHits) {
        return (int) Math.floor((1.0D / ((double) maxHits / (double) currentHits) * 14.0D));
    }
}
