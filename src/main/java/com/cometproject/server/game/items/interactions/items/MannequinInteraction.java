package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.items.data.MannequinData;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;

public class MannequinInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, GenericEntity avatar) {
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, GenericEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, GenericEntity avatar, boolean isWiredTriggered) {
        if(!(avatar instanceof PlayerEntity))
            return false;

        MannequinData data = MannequinData.get(item.getExtraData());

        if (data == null) {
            // There's no data to use!!
            return false;
        }

        ((PlayerEntity) avatar).getPlayer().getData().setFigure(data.getFigure());
        ((PlayerEntity) avatar).getPlayer().getData().setGender(data.getGender());

        ((PlayerEntity) avatar).getPlayer().getData().save();

        avatar.getRoom().getEntities().broadcastMessage(UpdateInfoMessageComposer.compose(avatar));
        return false;
    }

    @Override
    public boolean onPlace(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(RoomItem item, GenericEntity avatar, int updateState) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
