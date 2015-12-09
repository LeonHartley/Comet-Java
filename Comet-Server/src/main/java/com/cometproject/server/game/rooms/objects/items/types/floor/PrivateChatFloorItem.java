package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.GenericFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.google.common.collect.Lists;

import java.util.List;

public class PrivateChatFloorItem extends GenericFloorItem {

    private List<PlayerEntity> entities = Lists.newArrayList();

    public PrivateChatFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        if(!(entity instanceof PlayerEntity) || this.entities.contains(entity)) return;

        entity.setPrivateChatItemId(this.getId());
        this.entities.add((PlayerEntity) entity);
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        if(!(entity instanceof PlayerEntity)) return;

        entity.setPrivateChatItemId(0);
        this.entities.remove(entity);
    }

    public void broadcastMessage(MessageComposer msg) {
        for(PlayerEntity playerEntity : this.entities) {
            playerEntity.getPlayer().getSession().send(msg);
        }
    }
}
