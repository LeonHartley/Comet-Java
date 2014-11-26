package com.cometproject.server.game.rooms.objects.items.types.floor.snowboarding;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.items.types.floor.AdjustableHeightFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import org.apache.commons.lang.StringUtils;

public class SnowboardSlopeFloorItem extends AdjustableHeightFloorItem {
    private static final int SNOWBOARD_MALE = 97;
    private static final int SNOWBOARD_FEMALE = 97;

    public SnowboardSlopeFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        if(entity.getCurrentEffect() == null || entity.getCurrentEffect().getEffectId() != getEffectId(entity.getGender())) {
            entity.applyEffect(new PlayerEffect(getEffectId(entity.getGender()), 0));
        }
    }

    private static int getEffectId(String gender) {
        if(gender.toLowerCase().equals("m")) {
            return SNOWBOARD_MALE;
        }

        return SNOWBOARD_FEMALE;
    }
}
