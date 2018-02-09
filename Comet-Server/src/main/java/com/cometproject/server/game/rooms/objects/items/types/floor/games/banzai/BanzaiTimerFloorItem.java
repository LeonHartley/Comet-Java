package com.cometproject.server.game.rooms.objects.items.types.floor.games.banzai;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.AbstractGameTimerFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import org.apache.commons.lang.StringUtils;


public class BanzaiTimerFloorItem extends AbstractGameTimerFloorItem {

    public BanzaiTimerFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public GameType getGameType() {
        return GameType.BANZAI;
    }
}
