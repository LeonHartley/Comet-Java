package com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.utilities.RandomUtil;


public class WiredAddonColourWheel extends RoomItemFloor {
    private static final int TIMEOUT = 4;

    public WiredAddonColourWheel(RoomItemData itemData, Room room) {
        super(itemData, room);

        this.getItemData().setData("0");
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger && entity != null) {
            if (!this.getPosition().touching(entity.getPosition())) {
                entity.moveTo(this.getPosition().squareBehind(this.getRotation()).getX(), this.getPosition().squareBehind(this.getRotation()).getY());
                return true;
            }
        }

        this.getItemData().setData("9");
        this.sendUpdate();

        this.setTicks(RoomItemFactory.getProcessTime(TIMEOUT / 2));
        return true;
    }

    @Override
    public void onTickComplete() {
        final int randomInteger = RandomUtil.getRandomInt(1, 8);

        this.getItemData().setData(randomInteger + "");
        this.sendUpdate();
    }
}
