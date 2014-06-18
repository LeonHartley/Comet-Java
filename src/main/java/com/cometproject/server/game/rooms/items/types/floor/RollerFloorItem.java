package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFactory;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;

import java.util.ArrayList;
import java.util.List;

public class RollerFloorItem extends RoomItemFloor {
    public RollerFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        if (this.ticksTimer < 1) {
            this.setTicks(RoomItemFactory.getProcessTime(2));
        }
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {

    }

    @Override
    public void onTickComplete() {
        this.handleEntities();
    }

    private void handleEntities() {
        Position3D sqInfront = this.squareInfront();

        if (!this.getRoom().getMapping().isValidPosition(sqInfront)) {
            return;
        }

        List<Integer> processedEntities = new ArrayList<>();

        List<GenericEntity> entities = this.getRoom().getEntities().getEntitiesAt(this.getX(), this.getY());

        for (GenericEntity entity : entities) {
            processedEntities.add(entity.getVirtualId());

            if (entity.getPosition().getX() != this.getX() && entity.getPosition().getY() != this.getY()) {
                continue;
            }

            if (!this.getRoom().getMapping().isValidStep(entity.getPosition(), sqInfront, true) || !this.getRoom().getEntities().isSquareAvailable(sqInfront.getX(), sqInfront.getY())) {
                this.setTicks(3);
                break;
            }

            if (entity.isWalking()) {
                continue;
            }

            double toHeight = 0d;

            for (RoomItemFloor nextItem : this.getRoom().getItems().getItemsOnSquare(sqInfront.getX(), sqInfront.getY())) {
                if (!nextItem.getDefinition().canSit) {
                    toHeight += nextItem.getDefinition().getHeight();
                }
            }

            entity.updateAndSetPosition(new Position3D(sqInfront.getX(), sqInfront.getY(), toHeight));
            this.getRoom().getEntities().broadcastMessage(SlideObjectBundleMessageComposer.compose(entity.getPosition(), new Position3D(sqInfront.getX(), sqInfront.getY(), toHeight), this.getId(), entity.getVirtualId(), 0));
        }
    }
}
