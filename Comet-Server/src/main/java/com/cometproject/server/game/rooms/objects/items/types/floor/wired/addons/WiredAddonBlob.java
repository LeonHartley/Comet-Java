package com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;

public class WiredAddonBlob extends RoomItemFloor {

    public WiredAddonBlob(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        this.getItemData().setData("1");
    }

    public void onGameStarted() {
        if (this.getRoom().getGame().getBlobCounter().get() < 2) {
            this.getRoom().getGame().getBlobCounter().incrementAndGet();

            this.getItemData().setData("0");
            this.sendUpdate();
        }
    }

    public void hideBlob() {
        this.getRoom().getGame().getBlobCounter().decrementAndGet();

        this.getItemData().setData("1");
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        // award point

        if (!(entity instanceof PlayerEntity) || this.getItemData().getData().equals("1")) {
            return;
        }

        final PlayerEntity playerEntity = (PlayerEntity) entity;

        if (playerEntity.getGameTeam() == GameTeam.NONE) {
            return;
        }

        // reward the team (check which one it is first!)
        this.getRoom().getGame().increaseScore(playerEntity.getGameTeam(), 1);
        this.hideBlob();
    }
}
