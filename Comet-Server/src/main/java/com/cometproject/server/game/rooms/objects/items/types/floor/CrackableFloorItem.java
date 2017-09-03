package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class CrackableFloorItem extends RoomItemFloor {

    public CrackableFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);

        this.setExtraData("0");
    }

    @Override
    public boolean onInteract(RoomEntity entity, int state, boolean isWiredTrigger) {
        int hits = Integer.parseInt(this.getExtraData());
        int maxHits = 20;

        if (hits <= maxHits) {
            hits++;
        } else {
            // we're open!
        }

        this.setExtraData(hits);
        this.sendUpdate();

        return true;
    }

    public void composeData(IComposer msg) {
        msg.writeInt(7);

        int state = Integer.parseInt(this.getExtraData());

        msg.writeString(this.calculateState(20, state));
        msg.writeInt(state);//state
        msg.writeInt(20);//max
    }

    private int calculateState(int maxHits, int currentHits) {
        return (int) Math.floor((1.0D / ((double) maxHits / (double) currentHits) * 14.0D));
    }
}
