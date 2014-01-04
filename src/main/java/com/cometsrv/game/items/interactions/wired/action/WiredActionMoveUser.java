package com.cometsrv.game.items.interactions.wired.action;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.items.RoomItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.wired.WiredStaticConfig;
import com.cometsrv.game.wired.data.WiredDataFactory;
import com.cometsrv.game.wired.data.effects.TeleportToItemData;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class WiredActionMoveUser extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, Avatar avatar) {
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
    public boolean onPlace(RoomItem item, Avatar avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, Avatar avatar, Room room) {
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
