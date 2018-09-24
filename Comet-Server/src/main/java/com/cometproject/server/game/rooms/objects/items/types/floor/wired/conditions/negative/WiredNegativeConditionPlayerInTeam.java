package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.WiredConditionPlayerInTeam;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.WiredConditionStuffIs;
import com.cometproject.server.game.rooms.types.Room;

public class WiredNegativeConditionPlayerInTeam extends WiredConditionPlayerInTeam {
    public WiredNegativeConditionPlayerInTeam(RoomItemData itemData, Room room) {
        super(itemData, room);
    }
}
