package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredItemSnapshot;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredConditionMatchSnapshot extends WiredConditionItem {
    private static final int PARAM_MATCH_STATE = 0;
    private static final int PARAM_MATCH_ROTATION = 1;
    private static final int PARAM_MATCH_POSITION = 2;

    public WiredConditionMatchSnapshot(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public int getInterface() {
        return 0;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (this.getWiredData().getParams().size() != 3) {
            return false;
        }

        final boolean matchState = this.getWiredData().getParams().get(PARAM_MATCH_STATE) == 1;
        final boolean matchRotation = this.getWiredData().getParams().get(PARAM_MATCH_ROTATION) == 1;
        final boolean matchPosition = this.getWiredData().getParams().get(PARAM_MATCH_POSITION) == 1;

        for (long itemId : this.getWiredData().getSelectedIds()) {
            RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);
            WiredItemSnapshot snapshot = this.getWiredData().getSnapshots().get(itemId);

            if (floorItem != null && snapshot != null) {
                boolean matchesState = !this.isNegative;
                boolean matchesRotation = !this.isNegative;
                boolean matchesPosition = !this.isNegative;

                if (matchState) {
                    if (!floorItem.getItemData().getData().equals(snapshot.getExtraData())) {
                        matchesState = false;
                    } else if (this.isNegative) {
                        matchesState = true;
                    }
                }

                if (matchRotation) {
                    if (floorItem.getRotation() != snapshot.getRotation()) {
                        matchesRotation = false;
                    } else if (this.isNegative) {
                        matchesRotation = true;
                    }
                }

                if (matchPosition) {
                    if (floorItem.getPosition().getX() != snapshot.getX() || floorItem.getPosition().getY() != snapshot.getY()) {
                        matchesPosition = false;
                    } else if (this.isNegative) {
                        matchesPosition = true;
                    }
                }

                if (this.isNegative && (matchesPosition || matchesRotation || matchesState)) {
                    return false;
                } else if (!this.isNegative && (!matchesPosition || !matchesRotation || !matchesState)) {
                    return false;
                }
            }
        }

        return true;
    }
}
