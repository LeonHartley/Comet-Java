package com.cometproject.server.game.rooms.items;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.items.types.GenericFloorItem;
import com.cometproject.server.game.rooms.items.types.GenericWallItem;
import com.cometproject.server.game.rooms.items.types.floor.*;
import com.cometproject.server.game.rooms.items.types.floor.banzai.BanzaiTeleporterFloorItem;
import com.cometproject.server.game.rooms.items.types.floor.boutique.MannequinFloorItem;
import com.cometproject.server.game.rooms.items.types.floor.hollywood.HaloTileFloorItem;
import com.cometproject.server.game.rooms.items.types.floor.summer.SummerShowerFloorItem;
import com.cometproject.server.game.rooms.items.types.wall.WheelWallItem;
import com.cometproject.server.game.rooms.items.types.wired.action.WiredActionMoveRotate;
import com.cometproject.server.game.rooms.items.types.wired.action.WiredActionMoveUser;
import com.cometproject.server.game.rooms.items.types.wired.action.WiredActionShowMessage;
import com.cometproject.server.game.rooms.items.types.wired.action.WiredActionToggleFurni;
import com.cometproject.server.game.rooms.items.types.wired.trigger.*;

public class RoomItemFactory {
    private static final int processMs = Integer.parseInt(Comet.getServer().getConfig().get("comet.system.item_process.interval"));

    public static RoomItemFloor createFloor(int id, int baseId, int roomId, int ownerId, int x, int y, double height, int rot, String data) {
        ItemDefinition def = CometManager.getItems().getDefintionNullable(baseId);
        if (def == null) {
            return null;
        }

        if (def.canSit) {
            return new SeatFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
        }

        switch (def.getInteraction()) {
            case "roller": {
                return new RollerFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "dice": {
                return new DiceFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "teleport": {
                return new TeleporterFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "onewaygate": {
                return new OneWayGateFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "gate": {
                return new GateFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "ball": {
                return new BallFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "roombg": {
                return new BackgroundTonerFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "bed": {
                return new BedFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "vendingmachine": {
                return new VendingMachineFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "mannequin": {
                return new MannequinFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "beach_shower": {
                return new SummerShowerFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "halo_tile": {
                return new HaloTileFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            // Wired
            case "wf_act_moverotate": {
                return new WiredActionMoveRotate(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "wf_act_moveuser": {
                return new WiredActionMoveUser(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "wf_act_saymsg": {
                return new WiredActionShowMessage(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "wf_act_togglefurni": {
                return new WiredActionToggleFurni(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "wf_trg_onsay": {
                return new WiredTriggerOnSay(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "wf_trg_enterroom": {
                return new WiredTriggerEnterRoom(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "wf_trg_onfurni": {
                return new WiredTriggerOnFurni(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            case "wf_trg_offfurni": {
                return new WiredTriggerOffFurni(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }
            case "wf_trg_timer": {
                return new WiredTriggerTimer(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            // Banzai
            case "bb_teleport": {
                return new BanzaiTeleporterFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }

            default: {
                return new GenericFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
            }
        }
    }

    public static RoomItemWall createWall(int id, int baseId, int roomId, int owner, String position, String data) {
        ItemDefinition def = CometManager.getItems().getDefintionNullable(baseId);
        if (def == null) {
            return null;
        }

        switch (def.getInteraction()) {
            case "habbowheel": {
                return new WheelWallItem(id, baseId, roomId, owner, position, data);
            }
            //case "dimmer": { return new MoodlightWallItem(id, baseId, roomId, owner, position, data); }
            default: {
                return new GenericWallItem(id, baseId, roomId, owner, position, data);
            }
        }
    }

    public static int getProcessTime(double time) {
        long realTime = Math.round(time * 1000 / processMs);
        if (realTime < 1) {
            realTime = 1;
        }

        return (int) realTime;
    }
}
