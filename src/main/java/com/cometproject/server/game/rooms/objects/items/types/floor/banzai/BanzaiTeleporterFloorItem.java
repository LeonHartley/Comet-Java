package com.cometproject.server.game.rooms.objects.items.types.floor.banzai;

import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.utilities.RandomInteger;
import javolution.util.FastSet;

import java.util.Set;

public class BanzaiTeleporterFloorItem extends RoomItemFloor {
    private int stage = 0;

    private Position teleportPosition;
    private GenericEntity entity;

    public BanzaiTeleporterFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        if(this.entity != null) return; // wait yer turn

        if (entity.hasAttribute("warp")) {
            this.stage = 2;
            this.setTicks(RoomItemFactory.getProcessTime(0.5));

            entity.removeAttribute("warp");
            return;
        }

        this.entity = entity;

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

        this.teleportPosition = new Position(randomTeleporter.getPosition().getX(), randomTeleporter.getPosition().getY(), randomTeleporter.getTile().getWalkHeight());
        this.entity.setAttribute("warp", true);

        this.setExtraData("1");
        this.sendUpdate();

        this.stage = 1;

        entity.cancelWalk();
        this.setTicks(RoomItemFactory.getProcessTime(0.5));

        teleporters.clear();
    }

    @Override
    public void onTickComplete() {
        if(this.stage == 1) {
            this.entity.warp(this.teleportPosition);
            this.entity = null;
            this.teleportPosition = null;

            this.setTicks(RoomItemFactory.getProcessTime(0.5));
            this.stage = 0;
            return;
        } else if(this.stage == 2) {
            this.setExtraData("1");
            this.sendUpdate();

            this.setTicks(RoomItemFactory.getProcessTime(.5));
            this.stage = 0;
            return;
        }

        this.setExtraData("0");
        this.sendUpdate();
    }
}
