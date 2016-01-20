package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;


public class WiredTriggerScoreAchieved extends WiredTriggerItem {
    private static final int PARAM_SCORE_TO_ACHIEVE = 0;

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
    public WiredTriggerScoreAchieved(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean suppliesPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 10;
    }

    public int scoreToAchieve() {
        if (this.getWiredData().getParams().size() == 1) {
            return this.getWiredData().getParams().get(PARAM_SCORE_TO_ACHIEVE);
        }

        return 0;
    }

    public static boolean executeTriggers(int score, GameTeam team, Room room) {
        boolean wasExecuted = false;

        for (RoomItemFloor floorItem : room.getItems().getByClass(WiredTriggerScoreAchieved.class)) {
            WiredTriggerScoreAchieved trigger = ((WiredTriggerScoreAchieved) floorItem);

            if (trigger.scoreToAchieve() == score) {
                wasExecuted = trigger.evaluate(null, team);
            }
        }

        return wasExecuted;
    }
}
