package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.google.common.collect.Lists;

import java.util.List;

public class PrivateChatFloorItem extends DefaultFloorItem {

    private List<PlayerEntity> entities = Lists.newArrayList();

    public PrivateChatFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (!(entity instanceof PlayerEntity) || this.entities.contains(entity)) return;

        entity.setPrivateChatItemId(this.getId());
        this.entities.add((PlayerEntity) entity);
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (!(entity instanceof PlayerEntity)) return;

        entity.setPrivateChatItemId(0);
        this.entities.remove(entity);
    }

    public void broadcastMessage(MessageComposer msg) {
        for (PlayerEntity playerEntity : this.entities) {
            playerEntity.getPlayer().getSession().send(msg);
        }
    }
}
