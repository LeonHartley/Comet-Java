package com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.utilities.RandomInteger;

public class WiredAddonPyramid extends RoomItemFloor {
    private boolean hasEntity = false;

    public WiredAddonPyramid(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        this.setTicks(RandomInteger.getRandom(5, 8) * 2);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        this.hasEntity = true;
    }


    @Override
    public void onEntityStepOff(GenericEntity entity) {
        this.hasEntity = true;
    }

    @Override
    public void onTickComplete() {
        if(this.hasEntity) {
            this.setTicks(2);
            return;
        }

        if(this.getExtraData().equals("1")) {
            this.setExtraData("0");
        } else {
            this.setExtraData("1");
        }

        this.sendUpdate();
        this.setTicks(RandomInteger.getRandom(5, 8) * 2);
        this.getRoom().getMapping().getTile(this.getPosition().getX(), this.getPosition().getY()).reload();
    }
}
