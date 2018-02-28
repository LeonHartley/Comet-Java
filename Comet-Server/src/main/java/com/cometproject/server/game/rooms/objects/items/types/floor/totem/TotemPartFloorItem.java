package com.cometproject.server.game.rooms.objects.items.types.floor.totem;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import org.apache.commons.lang.StringUtils;

public abstract class TotemPartFloorItem extends RoomItemFloor {
    public TotemPartFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        if (!StringUtils.isNumeric(this.getItemData().getData())) {
            this.getItemData().setData("0");
        }
    }

    public static int getDarkHead(int lightHead) {
        switch (lightHead) {

        }

        return 0;
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (this.isComplete()) {
            // find combinations then give the effect depending on it!
        } else {
            if (this instanceof TotemHeadFloorItem) {
                int newTotum = Integer.parseInt(this.getItemData().getData());


                this.getItemData().setData(newTotum + "");
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
            if (!StringUtils.isNumeric(this.getItemData().getData())) {
                this.getItemData().setData("0");
            }

            floorItem.getItemData().setData(String.valueOf(Integer.parseInt(this.getItemData().getData()) + 5)); // test

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
        int totemState = Integer.parseInt(this.getItemData().getData());

        this.getItemData().setData("" + TotemPartFloorItem.getDarkHead(totemState));

        this.sendUpdate();
        this.saveData();
    }
}
