package com.cometproject.server.game.rooms.items;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.items.types.GenericFloorItem;
import com.cometproject.server.game.rooms.items.types.floor.DiceFloorItem;
import com.cometproject.server.game.rooms.items.types.floor.RollerFloorItem;
import com.cometproject.server.game.rooms.items.types.floor.SeatFloorItem;

public class RoomItemFactory {

    public static RoomItemFloor createFloor(int id, int baseId, int roomId, int ownerId, int x, int y, double height, int rot, String data) {
        ItemDefinition def = CometManager.getItems().getDefintion(baseId);
        if (def == null) { return null; }

        if (def.canSit) {
            return new SeatFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
        }

        switch (def.getInteraction()) {
            case "roller":
                return new RollerFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);

            case "dice":
                return new DiceFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);

            default:
                return new GenericFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
        }
    }

    public static RoomItemWall createWall(int id, int baseId, int roomId, int owner, String position, String data) {
        return null;
    }
}
