package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;


public class WiredActionKickUser extends WiredActionShowMessage {

    public WiredActionKickUser(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        this.isWhisperBubble = true;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (event.entity != null && event.type == 1) {
            event.entity.leaveRoom(false, true, true);
            return;
        }

        if (!(event.entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity playerEntity = (PlayerEntity) event.entity;

        String kickException = "";

        if (this.getRoom().getData().getOwnerId() == playerEntity.getPlayerId()) {
            kickException = "Room owner";
        }

        if (kickException.isEmpty()) {
            super.onEventComplete(event);

            event.entity.applyEffect(new PlayerEffect(4, 5));
            event.type = 1;

            event.setTotalTicks(RoomItemFactory.getProcessTime(0.9));
            this.queueEvent(event);
        } else {
            playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(playerEntity.getId(), "Wired kick exception: " + kickException));
        }
    }
}
