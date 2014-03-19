package com.cometproject.server.game.items.interactions.wired.action;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.WiredStaticConfig;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class WiredActionToggleFurni extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar, boolean isWiredTriggered) {
        if (!(item instanceof FloorItem)) {
            return false;
        }

        FloorItem floorItem = (FloorItem) item;

        WiredDataInstance data = WiredDataFactory.get(floorItem);

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
    public boolean onTick(RoomItem item, PlayerEntity avatar, int updateState) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
