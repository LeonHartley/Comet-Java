package com.cometproject.game.items.interactions.wired.action;

import com.cometproject.game.GameEngine;
import com.cometproject.game.items.interactions.Interactor;
import com.cometproject.game.rooms.entities.types.PlayerEntity;
import com.cometproject.game.rooms.items.FloorItem;
import com.cometproject.game.rooms.items.RoomItem;
import com.cometproject.game.rooms.types.Room;
import com.cometproject.game.wired.WiredStaticConfig;
import com.cometproject.game.wired.data.WiredDataFactory;
import com.cometproject.game.wired.data.effects.TeleportToItemData;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class WiredActionMoveUser extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar) {
        if (!(item instanceof FloorItem)) {
            return false;
        }

        FloorItem floorItem = (FloorItem) item;

        TeleportToItemData data = (TeleportToItemData) WiredDataFactory.get(floorItem);

        if(data == null) {
            GameEngine.getLogger().debug("Failed to find WiredDataInstance for item: " + item.getId());
            return false;
        }

        Composer msg = new Composer(Composers.WiredEffectMessageComposer);

        msg.writeBoolean(false);
        msg.writeInt(WiredStaticConfig.MAX_FURNI_SELECTION);

        msg.writeInt(data.getCount());

        for(int itemId : data.getItems()){
            msg.writeInt(itemId);
        }

        msg.writeInt(item.getDefinition().getSpriteId());
        msg.writeInt(item.getId());

        msg.writeString("");
        msg.writeInt(0);
        msg.writeInt(8);
        msg.writeInt(0);
        msg.writeInt(data.getDelay());
        msg.writeInt(0);
        msg.writeString("");

        avatar.getPlayer().getSession().send(msg);
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
    public boolean onTick(RoomItem item) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return true;
    }
}
