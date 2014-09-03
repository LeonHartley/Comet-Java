package com.cometproject.server.game.rooms.items.types.floor.football;

import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;

public class FootballScoreFloorItem extends RoomItemFloor {
    private int score = 0;
    private GameTeam gameTeam;

    public FootballScoreFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);

        this.setExtraData("0");

        switch (this.getDefinition().getItemName()) {
            case "fball_score_b":
                this.gameTeam = GameTeam.BLUE;
            case "fball_score_r":
                this.gameTeam = GameTeam.RED;
            case "fball_score_y":
                this.gameTeam = GameTeam.YELLOW;
            case "fball_score_g":
                this.gameTeam = GameTeam.GREEN;
        }
    }

    public void increaseScore(GameTeam team) {
        if(this.gameTeam == team) {
            this.score++;

            this.setExtraData(this.score + "");
            this.sendUpdate();
        }
    }

    public void reset() {
        this.score = 0;

        this.setExtraData(this.score + "");
        this.sendUpdate();
    }
}
