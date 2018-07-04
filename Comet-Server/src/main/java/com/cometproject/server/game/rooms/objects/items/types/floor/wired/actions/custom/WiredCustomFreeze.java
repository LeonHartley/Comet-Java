package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.tasks.CometThreadManager;

import java.util.concurrent.TimeUnit;


public class WiredCustomFreeze extends WiredActionItem {

    public WiredCustomFreeze(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 1;
    }

    @Override
    public boolean usesDelay() {
        return false;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (!(event.entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity playerEntity = ((PlayerEntity) event.entity);

        if (playerEntity.getPlayer() == null || playerEntity.getPlayer().getSession() == null) {
            return;
        }

        freeze(playerEntity);

        CometThreadManager.getInstance().executeSchedule(() -> {
            freeze(playerEntity);
        }, this.getWiredData().getDelay() * 500, TimeUnit.MILLISECONDS);
    }

    private void freeze(PlayerEntity playerEntity) {
        playerEntity.cancelWalk();
        playerEntity.setCanWalk(!playerEntity.canWalk());

        if (!playerEntity.canWalk()) {
            playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(playerEntity.getId(), Locale.getOrDefault("command.massfreeze.frozen", "You've been frozen!"), 0));

            playerEntity.applyEffect(new PlayerEffect(12));
        } else {
            playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(playerEntity.getId(), Locale.getOrDefault("command.massfreeze.unfrozen", "You've been unfrozen!"), 0));

            playerEntity.applyEffect(playerEntity.getLastEffect());
        }
    }
}
