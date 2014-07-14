package com.cometproject.server.game.rooms.items.types.floor.banzai;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFactory;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.utilities.RandomInteger;
import javolution.util.FastSet;

import java.util.Set;

public class BanzaiTeleporterFloorItem extends RoomItemFloor {
    public BanzaiTeleporterFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        if(entity.hasAttribute("banzaiTeleport")) {
            entity.removeAttribute("banzaiTeleport");
            return;
        }

        Set<BanzaiTeleporterFloorItem> teleporters = new FastSet<>();

        for (RoomItemFloor tele : this.getRoom().getItems().getFloorItems()) {
            if (tele instanceof BanzaiTeleporterFloorItem) {
                if (tele.getId() != this.getId())
                    teleporters.add((BanzaiTeleporterFloorItem) tele);
            }
        }

        if (teleporters.size() < 1)
            return;

        BanzaiTeleporterFloorItem randomTeleporter = (BanzaiTeleporterFloorItem) teleporters.toArray()[RandomInteger.getRandom(0, teleporters.size() - 1)];

        Position3D teleportPosition = new Position3D(randomTeleporter.getX(), randomTeleporter.getY(), randomTeleporter.getHeight());
        // Move to position!

        entity.updateAndSetPosition(teleportPosition);

        entity.setAttribute("banzaiTeleport", true);

        this.setExtraData("1");
        this.sendUpdate();

        this.setTicks(RoomItemFactory.getProcessTime(0.5));

        teleporters.clear();
    }

    @Override
    public void onTickComplete() {
        this.setExtraData("0");
        this.sendUpdate();
    }
}
