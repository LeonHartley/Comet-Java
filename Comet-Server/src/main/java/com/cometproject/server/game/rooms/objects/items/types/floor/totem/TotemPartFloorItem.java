package com.cometproject.server.game.rooms.objects.items.types.floor.totem;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import org.apache.commons.lang.StringUtils;

public abstract class TotemPartFloorItem extends RoomItemFloor {
    public TotemPartFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if (!StringUtils.isNumeric(this.getExtraData())) {
            this.setExtraData("0");
        }
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (this.isComplete()) {
            // find combinations then give the effect depending on it!
        } else {
            if (this instanceof TotemHeadFloorItem) {
                int newTotum = Integer.parseInt(this.getExtraData());


                this.setExtraData(newTotum + "");
            } else {
                this.toggleInteract(true);
            }

            this.sendUpdate();
            this.saveData();
        }

        return true;
    }

    @Override
    public void onItemAddedToStack(RoomItemFloor floorItem) {
        if (floorItem instanceof TotemHeadFloorItem && this instanceof TotemBodyFloorItem) {
            if (!StringUtils.isNumeric(this.getExtraData())) {
                this.setExtraData("0");
            }

            floorItem.setExtraData(String.valueOf(Integer.parseInt(this.getExtraData()) + 5)); // test

            floorItem.sendUpdate();
            floorItem.saveData();
        }
    }

    protected boolean isComplete() {
        boolean hasHead = (this instanceof TotemHeadFloorItem);
        boolean hasBody = (this instanceof TotemBodyFloorItem);
        boolean hasPlanet = (this instanceof TotemPlanetFloorItem);

        for (RoomItemFloor floorItem : this.getItemsOnStack()) {
            if (floorItem instanceof TotemHeadFloorItem) hasHead = true;
            if (floorItem instanceof TotemBodyFloorItem) hasBody = true;
            if (floorItem instanceof TotemPlanetFloorItem) hasPlanet = true;
        }

        return hasHead && hasBody && hasPlanet;
    }


    @Override
    public void onPositionChanged(Position newPosition) {
        int totemState = Integer.parseInt(this.getExtraData());

        this.setExtraData("" + TotemPartFloorItem.getDarkHead(totemState));

        this.sendUpdate();
        this.saveData();
    }

    public static int getDarkHead(int lightHead) {
        switch (lightHead) {

        }

        return 0;
    }
}
