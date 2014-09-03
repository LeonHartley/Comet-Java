package com.cometproject.server.game.rooms.items.types.floor.football;

import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;

public class FootballGoalFloorItem extends RoomItemFloor {
    private GameTeam gameTeam;

    public FootballGoalFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);

        switch(this.getDefinition().getItemName()) {
            case "fball_goal_b": this.gameTeam = GameTeam.BLUE;
            case "fball_goal_r": this.gameTeam = GameTeam.RED;
            case "fball_goal_y": this.gameTeam = GameTeam.YELLOW;
            case "fball_goal_g": this.gameTeam = GameTeam.GREEN;
        }
    }

    @Override
    public void onItemAddedToStack(RoomItemFloor floorItem) {
        // update score depending on the team.
    }
}
