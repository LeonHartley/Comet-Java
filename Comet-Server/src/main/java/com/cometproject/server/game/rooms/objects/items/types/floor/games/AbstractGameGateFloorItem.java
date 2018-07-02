package com.cometproject.server.game.rooms.objects.items.types.floor.games;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;


public abstract class AbstractGameGateFloorItem extends DefaultFloorItem {
    public AbstractGameGateFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onLoad() {
        this.getRoom().getGame().getGates().get(this.getTeam()).add(this);
    }

    @Override
    public void onUnload() {
        this.getRoom().getGame().getGates().get(this.getTeam()).remove(this);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (!(entity instanceof PlayerEntity)) return;

        PlayerEntity playerEntity = (PlayerEntity) entity;

        boolean isLeaveTeam = false;

        if (playerEntity.getGameTeam() != GameTeam.NONE && playerEntity.getGameTeam() != this.getTeam()) {
            GameTeam oldTeam = playerEntity.getGameTeam();

            this.getRoom().getGame().removeFromTeam(playerEntity);

            for (AbstractGameGateFloorItem timer : this.getRoom().getGame().getGates().get(this.getTeam())) {
                timer.getItemData().setData(this.getRoom().getGame().getTeams().get(oldTeam).size());
                timer.sendUpdate();
            }

        } else if (playerEntity.getGameTeam() == this.getTeam()) {
            this.getRoom().getGame().removeFromTeam(playerEntity);

            isLeaveTeam = true;
        }

        if (!isLeaveTeam) {
            this.getRoom().getGame().joinTeam(this.getTeam(), playerEntity);

            playerEntity.setGameTeam(this.getTeam());
            playerEntity.applyTeamEffect(new PlayerEffect(this.getTeam().getEffect(this.gameType()), 0));
        } else {
            playerEntity.setGameTeam(GameTeam.NONE);
            playerEntity.applyTeamEffect(null);
        }

        this.updateTeamCount();
    }

    @Override
    public void onEntityLeaveRoom(RoomEntity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = ((PlayerEntity) entity);

            if (playerEntity.getGameTeam() == this.getTeam()) {
                this.getRoom().getGame().removeFromTeam(playerEntity);
                this.updateTeamCount();
            }
        }
    }

    private void updateTeamCount() {
        this.getItemData().setData("" + this.getRoom().getGame().getTeams().get(this.getTeam()).size());
        this.sendUpdate();
    }

    @Override
    public boolean isMovementCancelled(RoomEntity entity) {
        if (!(entity instanceof PlayerEntity)) {
            return true;
        }

        final PlayerEntity playerEntity = (PlayerEntity) entity;

        if (this.getRoom().getGame().getInstance() != null && this.getRoom().getGame().getInstance().isActive()) {
            return playerEntity.getGameTeam() == null || playerEntity.getGameTeam() == GameTeam.NONE;
        }

        return false;
    }

    public abstract GameType gameType();

    public abstract GameTeam getTeam();
}
