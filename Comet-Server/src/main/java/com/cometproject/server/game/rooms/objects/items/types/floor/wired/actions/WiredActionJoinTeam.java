package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;


public class WiredActionJoinTeam extends WiredActionItem {
    private static final int PARAM_TEAM_ID = 0;

    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredActionJoinTeam(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if (this.getWiredData().getParams().size() != 1) {
            this.getWiredData().getParams().put(PARAM_TEAM_ID, 1); // team red
        }
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 9;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (entity == null || !(entity instanceof PlayerEntity)) {
            return false;
        }

        PlayerEntity playerEntity = ((PlayerEntity) entity);

        if (playerEntity.getGameTeam() != GameTeam.NONE) {
            return false; // entity already in a team!
        }

        if (this.getTeam() == GameTeam.NONE)
            return false;

        playerEntity.setGameTeam(this.getTeam());
        this.getRoom().getGame().getTeams().get(this.getTeam()).add(playerEntity.getPlayerId());
        playerEntity.applyEffect(new PlayerEffect(this.getTeam().getFreezeEffect(), false));
        return true;
    }

    private GameTeam getTeam() {
        switch (this.getWiredData().getParams().get(PARAM_TEAM_ID)) {

            case 1:
                return GameTeam.RED;
            case 2:
                return GameTeam.GREEN;
            case 3:
                return GameTeam.BLUE;
            case 4:
                return GameTeam.YELLOW;
        }

        return GameTeam.NONE;
    }
}
