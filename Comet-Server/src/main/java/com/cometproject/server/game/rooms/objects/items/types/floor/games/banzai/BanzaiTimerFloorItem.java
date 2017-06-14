package com.cometproject.server.game.rooms.objects.items.types.floor.games.banzai;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.AbstractGameTimerFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import org.apache.commons.lang.StringUtils;


public class BanzaiTimerFloorItem extends AbstractGameTimerFloorItem {

    public BanzaiTimerFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public GameType getGameType() {
        return GameType.BANZAI;
    }
}
