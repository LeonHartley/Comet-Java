package com.cometproject.server.game.rooms.items.types.wired.action;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class WiredActionShowMessage extends RoomItemFloor {
    private int state = -1;

    public WiredActionShowMessage(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
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

        Composer msg = new Composer(Composers.WiredActionMessageComposer);

        msg.writeBoolean(false);
        msg.writeInt(0);
        msg.writeInt(0); // item count ????
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeInt(this.getId());
        msg.writeString(this.getExtraData());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(7);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);

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
