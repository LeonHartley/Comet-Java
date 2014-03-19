package com.cometproject.server.game.items.interactions.banzai;

import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.utilities.RandomInteger;
import javolution.util.FastList;

import java.util.List;

public class BanzaiTeleportInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        if(!state) // walk off item
            return false;

        FloorItem floorItem = (FloorItem) item;
        List<FloorItem> teleporters = new FastList<>();

        for(FloorItem tele : floorItem.getRoom().getItems().getFloorItems()) {
            if(tele.getDefinition().getInteraction().equals(floorItem.getDefinition().getInteraction())) {
                if(tele.getId() != floorItem.getId())
                    teleporters.add(tele);
            }
        }

        if(teleporters.size() < 1)
            return false;

        FloorItem randomTeleporter = teleporters.get(RandomInteger.getRandom(0, teleporters.size() - 1));

        Position3D teleportPosition = new Position3D(randomTeleporter.getX(), randomTeleporter.getY(), randomTeleporter.getHeight());
        // Move to position!

        avatar.updateAndSetPosition(teleportPosition);
        avatar.markNeedsUpdate();
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar, boolean isWiredTriggered) {
        return false;
    }

    @Override
    public boolean onPlace(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(RoomItem item, PlayerEntity avatar, int updateState) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
