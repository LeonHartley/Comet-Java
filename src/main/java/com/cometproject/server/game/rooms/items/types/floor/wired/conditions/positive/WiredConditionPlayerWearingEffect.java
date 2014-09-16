package com.cometproject.server.game.rooms.items.types.floor.wired.conditions.positive;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.types.floor.wired.base.WiredConditionItem;
import org.apache.commons.lang.StringUtils;

public class WiredConditionPlayerWearingEffect extends WiredConditionItem {
    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param roomId   The ID of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredConditionPlayerWearingEffect(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public int getInterface() {
        return 12;
    }

    @Override
    public boolean evaluate(GenericEntity entity, Object data) {
        if(!StringUtils.isNumeric(this.getWiredData().getText())) {
            return false;
        }

        final int effectId = Integer.parseInt(this.getWiredData().getText());
        boolean isWearingEffect = false;

        if(entity.getCurrentEffect() != null) {
            if(entity.getCurrentEffect().getEffectId() == effectId) {
                isWearingEffect = true;
            }
        }

        return isNegative ? !isWearingEffect : isWearingEffect;
    }
}
