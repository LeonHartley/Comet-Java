package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.outgoing.room.items.lovelock.LoveLockWidgetMessageComposer;

public class LoveLockFloorItem extends RoomItemFloor {
    private int leftEntity = 0;
    private int rightEntity = 0;

    public LoveLockFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void composeItemData(IComposer msg) {
        final String[] loveLockData = this.getItemData().getData().split(String.valueOf((char) 5));
        msg.writeInt(0);
        msg.writeInt(2);

        msg.writeInt(loveLockData.length);

        for (int i = 0; i < loveLockData.length; i++) {
            msg.writeString(loveLockData[i]);
        }
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (isWiredTrigger || entity == null) {
            return false;
        }

        if (!(entity instanceof PlayerEntity)) {
            return false;
        }

        if (this.getItemData().getData().startsWith("1")) return false;

        Position leftPosition = null;
        Position rightPosition = null;

        switch (this.getRotation()) {
            case 2:
                leftPosition = new Position(this.getPosition().getX(), this.getPosition().getY() - 1);
                rightPosition = new Position(this.getPosition().getX(), this.getPosition().getY() + 1);
                break;

            case 4:
                leftPosition = new Position(this.getPosition().getX() + 1, this.getPosition().getY());
                rightPosition = new Position(this.getPosition().getX() - 1, this.getPosition().getY());
                break;
        }

        if (leftPosition == null) {
            return false; //right pos would also be null
        }

        final RoomTile leftTile = this.getRoom().getMapping().getTile(leftPosition);
        final RoomTile rightTile = this.getRoom().getMapping().getTile(rightPosition);

        if (leftTile == null || rightTile == null || leftTile.getEntities().size() != 1 || rightTile.getEntities().size() != 1)
            return false;

        RoomEntity leftEntity = leftTile.getEntity();
        RoomEntity rightEntity = rightTile.getEntity();

        if (leftEntity.getEntityType() != RoomEntityType.PLAYER || rightEntity.getEntityType() != RoomEntityType.PLAYER)
            return false;

        try {
            ((PlayerEntity) leftEntity).getPlayer().getSession().send(new LoveLockWidgetMessageComposer(this.getVirtualId()));
            ((PlayerEntity) rightEntity).getPlayer().getSession().send(new LoveLockWidgetMessageComposer(this.getVirtualId()));

            this.leftEntity = ((PlayerEntity) leftEntity).getPlayerId();
            this.rightEntity = ((PlayerEntity) rightEntity).getPlayerId();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public int getLeftEntity() {
        return leftEntity;
    }

    public int getRightEntity() {
        return rightEntity;
    }
}
