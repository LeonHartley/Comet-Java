package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.DiceFloorItem;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.sessions.Session;

public class DiceCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        PlayerEntity entity = client.getPlayer().getEntity();

        for (RoomTile tile : entity.getTile().getAllAdjacentTiles()) {
            for (RoomItemFloor floorItem : tile.getItems()) {
                if (floorItem instanceof DiceFloorItem) {
                    floorItem.onInteract(entity, floorItem.getItemData().getData().equals("0") ? 0 : -1, false);
                }
            }
        }
    }

    @Override
    public String getPermission() {
        return "dice_command";
    }

    @Override
    public String getParameter() {
        return null;
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.dice.description", "Toggles all surrounding dice");
    }
}
