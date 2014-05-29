package com.cometproject.server.game.rooms.items.types.wired.trigger;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.wired.WiredStaticConfig;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class WiredTriggerOffFurni extends RoomItemFloor {
    private int state = -1;

    public WiredTriggerOffFurni(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity p = (PlayerEntity) entity;

        if (!this.getRoom().getRights().hasRights(p.getPlayerId()) && !p.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        WiredDataInstance data = WiredDataFactory.get(this);

        if (data == null) {
            CometManager.getLogger().debug("Failed to find WiredDataInstance for item: " + this.getId());
            return;
        }

        Composer msg = new Composer(Composers.WiredEffectMessageComposer);

        msg.writeBoolean(false);
        msg.writeInt(WiredStaticConfig.MAX_FURNI_SELECTION);

        msg.writeInt(data.getCount());

        for (int itemId : data.getItems()) {
            msg.writeInt(itemId);
        }

        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeInt(this.getId());

        msg.writeString("");
        msg.writeInt(0);
        msg.writeInt(9); // wired type
        msg.writeInt(0);
        msg.writeInt(data.getDelay());
        msg.writeInt(0);
        msg.writeString("");

        p.getPlayer().getSession().send(msg);
    }

    @Override
    public void onTickComplete() {
        switch (this.state) {
            case 0:
                this.setExtraData("1");
                this.sendUpdate();

                this.state = 1;
                this.setTicks(2);
                break;

            case 1:
                this.setExtraData("0");
                this.sendUpdate();
                break;
        }
    }
}
