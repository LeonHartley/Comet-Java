package com.cometproject.server.game.rooms.objects.items.types.floor.snowboarding;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.server.utilities.RandomInteger;

public class SnowboardJumpFloorItem extends RoomItemFloor {
    public SnowboardJumpFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        Position tileGoal = this.getPartnerTile();

        boolean increaseY = false;
        boolean decreaseY = false;
        boolean increaseX = false;
        boolean decreaseX = false;

        if (tileGoal != null) {
            switch (this.rotation) {
                case 4:
                    increaseY = true;
                    break;

                case 0:
                    decreaseY = true;
                    break;

                case 6:
                    decreaseX = true;
                    break;

                case 2:
                    increaseX = true;
                    break;
            }

            entity.moveTo(tileGoal.getX() + (increaseX ? 1 : decreaseX ? -1 : 0), tileGoal.getY() + (increaseY ? 1 : decreaseY ? -1 : 0));
            this.getRoom().getEntities().broadcastMessage(ActionMessageComposer.compose(entity.getId(), 8));
        }
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity playerEntity = ((PlayerEntity) entity);

        // Random!
        int actionId = RandomInteger.getRandom(9, 10);
        this.getRoom().getEntities().broadcastMessage(ActionMessageComposer.compose(playerEntity.getId(), actionId));
    }
}
