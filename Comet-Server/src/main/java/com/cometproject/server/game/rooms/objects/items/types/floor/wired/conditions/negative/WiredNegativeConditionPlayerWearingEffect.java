package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.WiredConditionPlayerWearingEffect;
import com.cometproject.server.game.rooms.types.Room;


public class WiredNegativeConditionPlayerWearingEffect extends WiredConditionPlayerWearingEffect {

    public WiredNegativeConditionPlayerWearingEffect(RoomItemData itemData, Room room) {
        super(itemData, room);
    }
}
