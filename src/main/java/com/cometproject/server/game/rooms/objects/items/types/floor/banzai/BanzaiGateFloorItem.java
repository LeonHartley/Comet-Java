package com.cometproject.server.game.rooms.objects.items.types.floor.banzai;

import com.cometproject.server.game.rooms.objects.items.types.GenericFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;

public class BanzaiGateFloorItem extends GenericFloorItem {
    private GameTeam gameTeam;

    public BanzaiGateFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        switch(this.getDefinition().getInteraction()) {
            case "bb_red_gate": gameTeam = GameTeam.RED; break;
            case "bb_blue_gate": gameTeam = GameTeam.BLUE; break;
            case "bb_green_gate": gameTeam = GameTeam.GREEN; break;
            case "bb_yellow_gate": gameTeam = GameTeam.YELLOW; break;
        }
    }

    public GameTeam getTeam() {
        return gameTeam;
    }
}
