package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;

public class WiredConditionPlayerInTeam extends WiredConditionItem {
    private static int PARAM_TEAM = 0;

    /**
     * The default constructor
     *
     * @param id        The ID of the item
     * @param itemId    The ID of the item definition
     * @param room      The instance of the room
     * @param owner     The ID of the owner
     * @param ownerName The username of the owner
     * @param x         The position of the item on the X axis
     * @param y         The position of the item on the Y axis
     * @param z         The position of the item on the z axis
     * @param rotation  The orientation of the item
     * @param data      The JSON object associated with this item
     */
    public WiredConditionPlayerInTeam(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public int getInterface() {
        return 6;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if(this.getWiredData().getParams().size() != 1) {
            return false;
        }

        if(!(entity instanceof PlayerEntity)) {
            return false;
        }

        final PlayerEntity playerEntity = ((PlayerEntity) entity);

        final int team = this.getWiredData().getParams().get(PARAM_TEAM);

        return playerEntity.getGameTeam() != null && playerEntity.getGameTeam().getTeamId() == team;
    }
}
