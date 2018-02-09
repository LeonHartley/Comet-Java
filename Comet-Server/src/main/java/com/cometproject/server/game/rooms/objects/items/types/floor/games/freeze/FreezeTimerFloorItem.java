package com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.AbstractGameTimerFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameType;

public class FreezeTimerFloorItem extends AbstractGameTimerFloorItem {
    public FreezeTimerFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public GameType getGameType() {
        return GameType.FREEZE;
    }
}
