package com.cometproject.server.game.rooms.objects.items.types.floor.banzai;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;


public class BanzaiGateFloorItem extends DefaultFloorItem {
    private GameTeam gameTeam;

    public BanzaiGateFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        switch (this.getDefinition().getInteraction()) {
            case "bb_red_gate":
                gameTeam = GameTeam.RED;
                break;
            case "bb_blue_gate":
                gameTeam = GameTeam.BLUE;
                break;
            case "bb_green_gate":
                gameTeam = GameTeam.GREEN;
                break;
            case "bb_yellow_gate":
                gameTeam = GameTeam.YELLOW;
                break;
        }
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (!(entity instanceof PlayerEntity)) return;

        PlayerEntity playerEntity = (PlayerEntity) entity;

        boolean isLeaveTeam = false;

        if (playerEntity.getGameTeam() != GameTeam.NONE && playerEntity.getGameTeam() != this.getTeam()) {
            GameTeam oldTeam = playerEntity.getGameTeam();

            this.getRoom().getGame().removeFromTeam(oldTeam, playerEntity.getPlayerId());

            for (RoomItemFloor floorItem : this.getRoom().getItems().getByInteraction("bb_" + oldTeam.toString().toLowerCase() + "_gate")) {
                floorItem.setExtraData("" + this.getRoom().getGame().getTeams().get(oldTeam).size());
                floorItem.sendUpdate();
            }
        } else if (playerEntity.getGameTeam() == this.getTeam()) {
            this.getRoom().getGame().removeFromTeam(this.getTeam(), playerEntity.getPlayerId());

            isLeaveTeam = true;
        }

        if (!isLeaveTeam) {
            this.getRoom().getGame().getTeams().get(this.getTeam()).add(playerEntity.getPlayerId());

            playerEntity.setGameTeam(this.getTeam());
            playerEntity.applyEffect(new PlayerEffect(this.getTeam().getBanzaiEffect(), 0));
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
                this.getRoom().getGame().removeFromTeam(this.getTeam(), playerEntity.getPlayerId());
                this.updateTeamCount();
            }
        }
    }

    private void updateTeamCount() {
        this.setExtraData("" + this.getRoom().getGame().getTeams().get(this.getTeam()).size());
        this.sendUpdate();
    }

    public GameTeam getTeam() {
        return gameTeam;
    }
}
