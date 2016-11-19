package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;


public class WiredActionKickUser extends WiredActionShowMessage {

    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredActionKickUser(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
        this.isWhisperBubble = true;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (!(entity instanceof PlayerEntity)) {
            return false;
        }

        PlayerEntity playerEntity = (PlayerEntity) entity;

        String kickException = "";

        if (this.getRoom().getData().getOwnerId() == playerEntity.getPlayerId()) {
            kickException = "Room owner";
        }

        if (kickException.isEmpty()) {
            if (super.evaluate(entity, data)) {
                final WiredItemEvent event = new WiredItemEvent();
                event.entity = entity;
                event.entity.applyEffect(new PlayerEffect(4, 5));

                event.setTotalTicks(RoomItemFactory.getProcessTime(0.9));
                this.queueEvent(event);
            }
        } else {
            playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(entity.getId(), "Wired kick exception: " + kickException));
        }

        return true;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (event.entity != null) {
            event.entity.leaveRoom(false, true, true);
        }
    }
}
