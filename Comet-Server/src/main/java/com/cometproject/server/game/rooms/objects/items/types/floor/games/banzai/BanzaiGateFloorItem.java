package com.cometproject.server.game.rooms.objects.items.types.floor.games.banzai;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.AbstractGameGateFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;


public class BanzaiGateFloorItem extends AbstractGameGateFloorItem {
    private GameTeam gameTeam;

    public BanzaiGateFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

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
    public GameType gameType() {
        return GameType.BANZAI;
    }

    @Override
    public GameTeam getTeam() {
        return gameTeam;
    }
}
