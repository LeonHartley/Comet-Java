package com.cometproject.server.game.rooms.objects.items.types.floor.football;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;


public class FootballScoreFloorItem extends RoomItemFloor {
    private GameTeam gameTeam;

    public FootballScoreFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        this.setExtraData("0");

        switch (this.getDefinition().getItemName()) {
            case "fball_score_b":
                this.gameTeam = GameTeam.BLUE;
                break;
            case "fball_score_r":
                this.gameTeam = GameTeam.RED;
                break;
            case "fball_score_y":
                this.gameTeam = GameTeam.YELLOW;
                break;
            case "fball_score_g":
                this.gameTeam = GameTeam.GREEN;
                break;
        }
    }

    public void sendUpdate() {
        this.setExtraData(this.getRoom().getGame().getScore(this.gameTeam) + "");

        super.sendUpdate();
    }

    public void reset() {
        this.setExtraData(0 + "");
        this.sendUpdate();
    }
}
