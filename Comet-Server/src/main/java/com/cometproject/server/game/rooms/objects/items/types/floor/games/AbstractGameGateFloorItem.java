package com.cometproject.server.game.rooms.objects.items.types.floor.games;

import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;


public abstract class AbstractGameGateFloorItem extends DefaultFloorItem {
    public AbstractGameGateFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public void onLoad() {
        if(!this.getRoom().getGame().getGates().get(this.getTeam()).contains(this)) {
            this.getRoom().getGame().getGates().get(this.getTeam()).add(this);
        }
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

            this.getRoom().getGame().removeFromTeam(oldTeam, playerEntity);

            for(AbstractGameGateFloorItem timer : this.getRoom().getGame().getGates().get(this.getTeam())) {
                timer.setExtraData("" + this.getRoom().getGame().getTeams().get(oldTeam).size());
                timer.sendUpdate();
            }

        } else if (playerEntity.getGameTeam() == this.getTeam()) {
            this.getRoom().getGame().removeFromTeam(this.getTeam(), playerEntity);

            isLeaveTeam = true;
        }

        if (!isLeaveTeam) {
            this.getRoom().getGame().joinTeam(this.getTeam(), playerEntity);

            playerEntity.setGameTeam(this.getTeam());
            playerEntity.applyEffect(new PlayerEffect(this.getTeam().getEffect(this.gameType()), 0));
        } else {
            playerEntity.setGameTeam(GameTeam.NONE);
            playerEntity.applyEffect(null);
        }

        this.updateTeamCount();
    }

    @Override
    public void onEntityLeaveRoom(RoomEntity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = ((PlayerEntity) entity);

            if (playerEntity.getGameTeam() == this.getTeam()) {
                this.getRoom().getGame().removeFromTeam(this.getTeam(), playerEntity);
                this.updateTeamCount();
            }
        }
    }

    private void updateTeamCount() {
        this.setExtraData("" + this.getRoom().getGame().getTeams().get(this.getTeam()).size());
        this.sendUpdate();
    }

    @Override
    public boolean isMovementCancelled(RoomEntity entity) {
        if(!(entity instanceof PlayerEntity)) {
            return true;
        }

        final PlayerEntity playerEntity = (PlayerEntity) entity;

        if (this.getRoom().getGame().getInstance() != null && this.getRoom().getGame().getInstance().isActive()) {
            if(playerEntity.getGameTeam() != null && playerEntity.getGameTeam() != GameTeam.NONE) {
                return false;
            }

            return true;
        }

        return false;
    }

    public abstract GameType gameType();

    public abstract GameTeam getTeam();
}
