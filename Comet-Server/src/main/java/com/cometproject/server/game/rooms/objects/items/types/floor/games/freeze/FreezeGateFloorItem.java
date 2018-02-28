package com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.AbstractGameGateFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.games.GameType;

public class FreezeGateFloorItem extends AbstractGameGateFloorItem {
    private final GameTeam gameTeam;

    public FreezeGateFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);

        final String itemName = this.getDefinition().getItemName();

        this.getItemData().setData("0");

        if (itemName.endsWith("y")) {
            this.gameTeam = GameTeam.YELLOW;
        } else if (itemName.endsWith("b")) {
            this.gameTeam = GameTeam.BLUE;
        } else if (itemName.endsWith("r")) {
            this.gameTeam = GameTeam.RED;
        } else {
            this.gameTeam = GameTeam.GREEN;
        }
    }

    @Override
    public GameType gameType() {
        return GameType.FREEZE;
    }

    @Override
    public GameTeam getTeam() {
        return this.gameTeam;
    }
}
