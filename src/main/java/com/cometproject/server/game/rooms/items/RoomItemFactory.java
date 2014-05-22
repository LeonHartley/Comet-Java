package com.cometproject.server.game.rooms.items;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.items.types.GenericFloorItem;
import com.cometproject.server.game.rooms.items.types.GenericWallItem;
import com.cometproject.server.game.rooms.items.types.floor.RollerFloorItem;

import java.util.HashMap;
import java.util.Map;

public class RoomItemFactory {
    private static final Map<String, Class<? extends RoomItem>> itemTypes = new HashMap<>();

    static {
        load();
    }

    public static void load() {
        itemTypes.put("roller", RollerFloorItem.class);
        System.out.println("added ??");
    }

    public static RoomItemFloor createFloor(int id, int baseId, int roomId, int ownerId, int x, int y, double height, int rot, String data) {
        ItemDefinition def = CometManager.getItems().getDefintion(baseId);

        try {
            if (itemTypes.containsKey(def.getInteraction())) {
                System.out.println("roller??");

                Class c = itemTypes.get(def.getInteraction());
                return (RoomItemFloor)c.getConstructor(c.getClass()).newInstance(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }
        } catch (Exception e) { }

        return new GenericFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
    }

    public static RoomItemWall createWall(int id, int baseId, int roomId, int owner, String position, String data) {
        ItemDefinition def = CometManager.getItems().getDefintion(baseId);

        try {
            if (itemTypes.containsKey(def.getInteraction())) {
                return (RoomItemWall) itemTypes.get(def.getInteraction()).getConstructor(RoomItemWall.class).newInstance(id, def.getId(), roomId, owner, position, data);
            }
        } catch (Exception e) { }

        return new GenericWallItem(id, baseId, roomId, owner, position, data);
    }
}
