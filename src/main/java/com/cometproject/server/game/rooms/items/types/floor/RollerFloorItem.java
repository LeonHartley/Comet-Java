package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFactory;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RollerFloorItem extends RoomItemFloor {
    private Map<Integer, GenericEntity> interactingEntities = new HashMap<>();

    public RollerFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        if (this.interactingEntities.containsKey(entity.getVirtualId())) { return; }
        this.interactingEntities.put(entity.getVirtualId(), entity);

        if (this.ticksTimer < 1) {
            this.setTicks(RoomItemFactory.getProcessTime(3));
        }
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        this.interactingEntities.remove(entity.getVirtualId());

        if (this.interactingEntities.size() == 0) {
            this.cancelTicks();
        }
    }

    @Override
    public void onTickComplete() {
        if (this.interactingEntities.size() == 0) { return; }

        Position3D sqInfront = this.squareInfront();

        if (!this.getRoom().getMapping().isValidPosition(sqInfront)) {
            return;
        }

        List<Integer> processedEntities = new ArrayList<>();

        for (GenericEntity entity : this.interactingEntities.values()) {
            processedEntities.add(entity.getVirtualId());

            if (!this.getRoom().getMapping().isValidStep(entity.getPosition(), sqInfront, true) || !this.getRoom().getEntities().isSquareAvailable(sqInfront.getX(), sqInfront.getY())) {
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

        for (Integer virtualId : processedEntities) {
            this.interactingEntities.remove(virtualId);
        }
    }
}
