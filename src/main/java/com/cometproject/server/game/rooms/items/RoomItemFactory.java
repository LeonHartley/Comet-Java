package com.cometproject.server.game.rooms.items;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.items.types.GenericFloorItem;
import com.cometproject.server.game.rooms.items.types.GenericWallItem;
import com.cometproject.server.game.rooms.items.types.floor.*;
import com.cometproject.server.game.rooms.items.types.floor.banzai.BanzaiTeleporterFloorItem;
import com.cometproject.server.game.rooms.items.types.floor.boutique.MannequinFloorItem;
import com.cometproject.server.game.rooms.items.types.floor.football.*;
import com.cometproject.server.game.rooms.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.game.rooms.items.types.floor.hollywood.HaloTileFloorItem;
import com.cometproject.server.game.rooms.items.types.floor.summer.SummerShowerFloorItem;
import com.cometproject.server.game.rooms.items.types.floor.wired.actions.WiredActionMatchToSnapshot;
import com.cometproject.server.game.rooms.items.types.floor.wired.actions.WiredActionTeleportPlayer;
import com.cometproject.server.game.rooms.items.types.floor.wired.conditions.negative.WiredNegativeConditionFurniHasPlayers;
import com.cometproject.server.game.rooms.items.types.floor.wired.conditions.positive.WiredConditionFurniHasPlayers;
import com.cometproject.server.game.rooms.items.types.floor.wired.conditions.positive.WiredConditionPlayerInGroup;
import com.cometproject.server.game.rooms.items.types.floor.wired.conditions.positive.WiredConditionTriggererOnFurni;
import com.cometproject.server.game.rooms.items.types.floor.wired.conditions.negative.WiredNegativeConditionPlayerInGroup;
import com.cometproject.server.game.rooms.items.types.floor.wired.triggers.*;
import com.cometproject.server.game.rooms.items.types.floor.wired.triggers.WiredTriggerEnterRoom;
import com.cometproject.server.game.rooms.items.types.wall.MoodlightWallItem;
import com.cometproject.server.game.rooms.items.types.wall.WheelWallItem;
import com.cometproject.server.game.rooms.items.types.wired.action.WiredActionMoveRotate;
import com.cometproject.server.game.rooms.items.types.wired.action.WiredActionShowMessage;
import com.cometproject.server.game.rooms.items.types.wired.action.WiredActionToggleFurni;
import com.cometproject.server.game.rooms.items.types.wired.trigger.WiredTriggerOffFurni;
import com.cometproject.server.game.rooms.items.types.wired.trigger.WiredTriggerOnFurni;
import com.cometproject.server.game.rooms.items.types.wired.trigger.WiredTriggerOnSay;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

public class RoomItemFactory {
    private static final int processMs = Integer.parseInt(Comet.getServer().getConfig().get("comet.system.item_process.interval"));
    private static final Logger log = Logger.getLogger(RoomItemFactory.class.getName());

    private static final FastMap<String, Class<? extends RoomItemFloor>> itemDefinitionMap;

    static {
        itemDefinitionMap = new FastMap<String, Class<? extends RoomItemFloor>>() {{
            put("roller", RollerFloorItem.class);
            put("dice", DiceFloorItem.class);
            put("teleport", TeleporterFloorItem.class);
            put("teleport_door", TeleporterFloorItem.class);
            put("onewaygate", OneWayGateFloorItem.class);
            put("gate", GateFloorItem.class);
            put("roombg", BackgroundTonerFloorItem.class);
            put("bed", BedFloorItem.class);
            put("vendingmachine", VendingMachineFloorItem.class);
            put("mannequin", MannequinFloorItem.class);
            put("beach_shower", SummerShowerFloorItem.class);
            put("halo_tile", HaloTileFloorItem.class);

//todo:start
            put("wf_act_match_to_sshot", WiredActionMatchToSnapshot.class);
//todo:end

            put("wf_act_teleport_to", WiredActionTeleportPlayer.class);//new

            put("wf_trg_says_something", WiredTriggerPlayerSaysKeyword.class);//new
            put("wf_trg_enter_room", WiredTriggerEnterRoom.class);//new
            put("wf_trg_periodically", WiredTriggerPeriodically.class);//new

            put("wf_cnd_trggrer_on_frn", WiredConditionTriggererOnFurni.class);//new
            put("wf_cnd_not_trggrer_on", WiredNegativeConditionPlayerInGroup.class);//new
            put("wf_cnd_actor_in_group", WiredConditionPlayerInGroup.class);//new
            put("wf_cnd_not_in_group", WiredNegativeConditionPlayerInGroup.class);//new
            put("wf_cnd_furnis_hv_avtrs", WiredConditionFurniHasPlayers.class);//new
            put("wf_cnd_not_hv_avtrs", WiredNegativeConditionFurniHasPlayers.class);//new

            put("wf_trg_onsay", WiredTriggerOnSay.class);//old
            put("wf_trg_onfurni", WiredTriggerOnFurni.class);//old
            put("wf_trg_offfurni", WiredTriggerOffFurni.class);//old

            put("wf_act_saymsg", WiredActionShowMessage.class);//old
            put("wf_act_togglefurni", WiredActionToggleFurni.class);//old
            put("wf_act_moverotate", WiredActionMoveRotate.class);//old

            put("bb_teleport", BanzaiTeleporterFloorItem.class);

            put("group_item", GroupFloorItem.class);

            put("football_timer", FootballTimerFloorItem.class);
            put("ball", FootballFloorItem.class);
            put("football_gate", FootballGateFloorItem.class);
            put("football_goal", FootballGoalFloorItem.class);
            put("football_score", FootballScoreFloorItem.class);
        }};
    }

    public static RoomItemFloor createFloor(int id, int baseId, int roomId, int ownerId, int x, int y, double height, int rot, String data) {
        ItemDefinition def = CometManager.getItems().getDefinition(baseId);
        RoomItemFloor floorItem = null;

        if (def == null) {
            return null;
        }

        if (def.canSit) {
            return new SeatFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
        }

        if (def.getItemName().startsWith("tile_stackmagic")) {
            floorItem = new MagicStackFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
        }

        if (itemDefinitionMap.containsKey(def.getInteraction())) {
            try {
                floorItem = itemDefinitionMap.get(def.getInteraction()).getConstructor(int.class, int.class, int.class, int.class, int.class, int.class, double.class, int.class, String.class)
                        .newInstance(id, baseId, roomId, ownerId, x, y, height, rot, data);
            } catch (Exception e) {
                log.warn("Failed to create instance for item: " + id + ", type: " + def.getInteraction());
                e.printStackTrace();
            }
        }

        if(floorItem == null) {
            floorItem = new GenericFloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
        }

        return floorItem;
    }

    public static RoomItemWall createWall(int id, int baseId, int roomId, int owner, String position, String data) {
        ItemDefinition def = CometManager.getItems().getDefinition(baseId);
        if (def == null) {
            return null;
        }

        switch (def.getInteraction()) {
            case "habbowheel": {
                return new WheelWallItem(id, baseId, roomId, owner, position, data);
            }
            case "dimmer": {
                return new MoodlightWallItem(id, baseId, roomId, owner, position, data);
            }
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
