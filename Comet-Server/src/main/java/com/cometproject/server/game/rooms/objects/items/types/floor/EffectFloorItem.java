package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class EffectFloorItem extends RoomItemFloor {
    private final int effectId;

    public EffectFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);

        this.effectId = this.getDefinition().getEffectId();
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if(!(entity instanceof PlayerEntity)) {
            return;
        }

        if(entity.getCurrentEffect() != null && entity.getCurrentEffect().getEffectId() == this.effectId) {
            entity.applyEffect(null);
        } else {
            entity.applyEffect(new PlayerEffect(this.effectId, 0));
        }
    }
}
