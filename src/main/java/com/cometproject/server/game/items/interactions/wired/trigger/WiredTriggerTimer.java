package com.cometproject.server.game.items.interactions.wired.trigger;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.items.interactions.InteractionAction;
import com.cometproject.server.game.items.interactions.InteractionQueueItem;
import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.WiredStaticConfig;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class WiredTriggerTimer extends Interactor {
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
        if (!(item instanceof FloorItem) || !(avatar instanceof PlayerEntity)) {
            return false;
        }

        FloorItem floorItem = (FloorItem) item;

        WiredDataInstance data = WiredDataFactory.get(floorItem);

        if (data == null) {
            CometManager.getLogger().debug("Failed to find WiredDataInstance for item: " + item.getId());
            return false;
        }

        Composer msg = new Composer(Composers.WiredTriggerMessageComposer);

        msg.writeBoolean(false);
        msg.writeInt(WiredStaticConfig.MAX_FURNI_SELECTION);
        msg.writeInt(0);

        msg.writeInt(item.getDefinition().getSpriteId());
        msg.writeInt(item.getId());
        msg.writeString("");

        msg.writeInt(1);
        msg.writeInt(data.getDelay()); // delay
        msg.writeInt(0);
        msg.writeInt(6);
        msg.writeInt(0);
        msg.writeInt(0);

        ((PlayerEntity) avatar).getPlayer().getSession().send(msg);
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
        switch (updateState) {
            case 0:
                ((FloorItem) item).sendData("1");
                item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 1, 2));
                break;

            case 1:
                ((FloorItem) item).sendData("0");
                break;
        }
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
