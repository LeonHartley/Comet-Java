package com.cometproject.server.game.rooms.items.types.floor.wired.addons;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.utilities.RandomInteger;

public class WiredAddonColourWheel extends RoomItemFloor {
    private static final int TIMEOUT = 4;

    public WiredAddonColourWheel(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, "0");
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if(!isWiredTrigger && entity != null) {
            if(!this.touching(entity)) {
                entity.moveTo(this.squareBehind().getX(), this.squareBehind().getY());
                return;
            }
        }

        this.setExtraData("9");
        this.sendUpdate();

        this.setTicks(TIMEOUT); // 2.5s
    }

    @Override
    public void onTickComplete() {
        final int randomInteger = RandomInteger.getRandom(1, 8);

        this.setExtraData(randomInteger + "");
        this.sendUpdate();
    }
}
