package com.cometproject.server.game.rooms.items.types.floor.football;

import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;

public class FootballGoalFloorItem extends RoomItemFloor {
    private GameTeam gameTeam;

    public FootballGoalFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);

        switch(this.getDefinition().getItemName()) {
            case "fball_goal_b":
                this.gameTeam = GameTeam.BLUE;
                break;
            case "fball_goal_r":
                this.gameTeam = GameTeam.RED;
                break;
            case "fball_goal_y":
                this.gameTeam = GameTeam.YELLOW;
                break;
            case "fball_goal_g":
                this.gameTeam = GameTeam.GREEN;
                break;
        }
    }

    @Override
    public void onItemAddedToStack(RoomItemFloor floorItem) {
        System.out.println("Goal team: " + gameTeam);

        for(RoomItemFloor scoreItem : this.getRoom().getItems().getByInteraction("football_score")) {
            ((FootballScoreFloorItem) scoreItem).increaseScore(this.gameTeam);
        }
    }

    public GameTeam getGameTeam() {
        return gameTeam;
    }
}
