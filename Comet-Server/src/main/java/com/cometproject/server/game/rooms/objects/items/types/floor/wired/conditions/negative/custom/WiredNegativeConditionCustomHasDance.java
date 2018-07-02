package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.WiredConditionPlayerWearingEffect;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.custom.WiredConditionCustomHasDance;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.custom.WiredConditionCustomHasDiamonds;
import com.cometproject.server.game.rooms.types.Room;


public class WiredNegativeConditionCustomHasDance extends WiredConditionCustomHasDance {

    public WiredNegativeConditionCustomHasDance(RoomItemData itemData, Room room) {
        super(itemData, room);
    }
}
