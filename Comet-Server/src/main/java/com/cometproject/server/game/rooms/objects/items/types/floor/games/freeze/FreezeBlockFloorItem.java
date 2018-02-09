package com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;
import com.cometproject.server.game.rooms.types.components.games.freeze.FreezeGame;
import com.cometproject.server.game.rooms.types.components.games.freeze.types.FreezePlayer;
import com.cometproject.server.game.rooms.types.components.games.freeze.types.FreezePowerUp;

public class FreezeBlockFloorItem extends RoomItemFloor {

    private boolean destroyed = false;
    private FreezePowerUp powerUp;

    public FreezeBlockFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        // if has power up, apply power up.
        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        final PlayerEntity playerEntity = ((PlayerEntity) entity);

        if (!this.isDestroyed()) {
            return;
        }

        final RoomGame game = this.getRoom().getGame().getInstance();

        if (this.getRoom().getGame().getInstance() == null || !(game instanceof FreezeGame)) {
            return;
        }

        final FreezeGame freezeGame = (FreezeGame) game;

        if (playerEntity.getGameTeam() == null || playerEntity.getGameTeam() == GameTeam.NONE) {
            return;
        }

        final FreezePlayer freezePlayer = freezeGame.freezePlayer(playerEntity.getPlayerId());

        freezePlayer.powerUp(this.powerUp);
        this.setPowerUp(FreezePowerUp.None);
    }

    private void setPowerUp(FreezePowerUp powerUp) {
        this.powerUp = powerUp;

        if (powerUp == FreezePowerUp.None && this.isDestroyed()) {
            this.updateState(powerUp, true);
        } else {
            this.updateState(powerUp, false);
        }
    }

    public void reset() {
        this.destroyed = false;
        this.powerUp = null;

        this.getItemData().setData("0");
        this.sendUpdate();

        this.getTile().reload();
    }

    public void explode() {
        if (this.destroyed) {
            return;
        }

        this.destroyed = true;

        this.setPowerUp(FreezePowerUp.getRandom());
        this.getTile().reload();
    }

    private void updateState(FreezePowerUp powerUp, boolean fadeAway) {
        int num = 0;
        if (powerUp != null) {
            num = (powerUp.ordinal() + 1) * 1000;
            if (fadeAway) {
                num += 10000;
            }
        }

        this.getItemData().setData(num);
        this.sendUpdate();
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
