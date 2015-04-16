package com.cometproject.server.game.rooms.objects.items.types.floor.football;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;


public class FootballGoalFloorItem extends RoomItemFloor {
    private GameTeam gameTeam;

    public FootballGoalFloorItem(int id, int itemId, RoomInstance room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        switch (this.getDefinition().getItemName()) {
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
        this.getRoom().getGame().increaseScore(this.gameTeam, 1);
    }

    public GameTeam getGameTeam() {
        return gameTeam;
    }
}
