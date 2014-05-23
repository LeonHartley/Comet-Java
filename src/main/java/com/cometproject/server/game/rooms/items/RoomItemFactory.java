package com.cometproject.server.game.rooms.items;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.items.types.GenericFloorItem;
import com.cometproject.server.game.rooms.items.types.GenericWallItem;
import com.cometproject.server.game.rooms.items.types.floor.*;
import com.cometproject.server.game.rooms.items.types.wall.WheelWallItem;

public class RoomItemFactory {

    public static RoomItemFloor createFloor(int id, int baseId, int roomId, int ownerId, int x, int y, double height, int rot, String data) {
        ItemDefinition def = CometManager.getItems().getDefintion(baseId);
        if (def == null) { return null; }

        if (def.canSit) {
            return new SeatFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
        }

        switch (def.getInteraction()) {
            case "roller": { return new RollerFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data); }
            case "dice": { return new DiceFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data); }
            case "teleport": { return new TeleporterFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data); }
            case "onewaygate": { return new OneWayGateFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data); }
            case "ball": { return new BallFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data); }
            case "roombg": { return new BackgroundTonerFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data); }
            case "bed": { return new BedFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data); }
            default: { return new GenericFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data); }
        }
    }

    public static RoomItemWall createWall(int id, int baseId, int roomId, int owner, String position, String data) {
        ItemDefinition def = CometManager.getItems().getDefintion(baseId);
        if (def == null) { return null; }

        switch (def.getInteraction()) {
            case "habbowheel": { return new WheelWallItem(id, baseId, roomId, owner, position, data); }
            default: { return new GenericWallItem(id, baseId, roomId, owner, position, data); }
        }
    }
}
