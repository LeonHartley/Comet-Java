package com.cometproject.server.game.rooms.objects.items;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.objects.items.types.GenericFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.GenericWallItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.banzai.BanzaiTeleporterFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.boutique.MannequinFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.football.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.hollywood.HaloTileFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.summer.SummerShowerFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons.WiredAddonColourWheel;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons.WiredAddonFloorSwitch;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons.WiredAddonPressurePlate;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons.WiredAddonPyramid;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerEnterRoom;
import com.cometproject.server.game.rooms.objects.items.types.wall.MoodlightWallItem;
import com.cometproject.server.game.rooms.objects.items.types.wall.WheelWallItem;
import com.cometproject.server.game.rooms.types.Room;
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

            put("wf_act_match_to_sshot", WiredActionMatchToSnapshot.class);//new
            put("wf_act_teleport_to", WiredActionTeleportPlayer.class);//new
            put("wf_act_show_message", WiredActionShowMessage.class);//new
            put("wf_act_toggle_state", WiredActionToggleState.class);//new
            put("wf_act_give_reward", WiredActionGiveReward.class);//new
            put("wf_act_move_rotate", WiredActionMoveRotate.class);//new

            put("wf_trg_says_something", WiredTriggerPlayerSaysKeyword.class);//new
            put("wf_trg_enter_room", WiredTriggerEnterRoom.class);//new
            put("wf_trg_periodically", WiredTriggerPeriodically.class);//new
            put("wf_trg_walks_off_furni", WiredTriggerWalksOffFurni.class);//new
            put("wf_trg_walks_on_furni", WiredTriggerWalksOnFurni.class);//new
            put("wf_trg_state_changed", WiredTriggerStateChanged.class);//new

            put("wf_cnd_trggrer_on_frn", WiredConditionTriggererOnFurni.class);//new
            put("wf_cnd_not_trggrer_on", WiredNegativeConditionPlayerInGroup.class);//new
            put("wf_cnd_actor_in_group", WiredConditionPlayerInGroup.class);//new
            put("wf_cnd_not_in_group", WiredNegativeConditionPlayerInGroup.class);//new
            put("wf_cnd_furnis_hv_avtrs", WiredConditionFurniHasPlayers.class);//new
            put("wf_cnd_not_hv_avtrs", WiredNegativeConditionFurniHasPlayers.class);//new
            put("wf_cnd_wearing_badge", WiredConditionPlayerHasBadgeEquipped.class);//new
            put("wf_cnd_not_wearing_badge", WiredNegativeConditionPlayerHasBadgeEquipped.class);//new
            put("wf_cnd_wearing_effect", WiredConditionPlayerWearingEffect.class);//new
            put("wf_cnd_not_wearing_effect", WiredNegativeConditionPlayerWearingEffect.class);//new
            put("wf_cnd_has_furni_on", WiredConditionHasFurniOn.class);//new
            put("wf_cnd_not_has_furni_on", WiredNegativeConditionHasFurniOn.class);//new
            put("wf_cnd_user_count_in", WiredConditionPlayerCountInRoom.class);//new
            put("wf_cnd_not_user_count", WiredConditionPlayerCountInRoom.class);//new
            put("wf_cnd_match_snapshot", WiredConditionMatchSnapshot.class);
            put("wf_cnd_not_match_snap", WiredNegativeMatchSnapshot.class);

            put("wf_floor_switch1", WiredAddonFloorSwitch.class);//new
            put("wf_floor_switch2", WiredAddonFloorSwitch.class);//new
            put("wf_colorwheel", WiredAddonColourWheel.class);//new
            put("wf_pressureplate", WiredAddonPressurePlate.class);//new
            put("wf_arrowplate", WiredAddonPressurePlate.class);//new
            put("wf_ringplate", WiredAddonPressurePlate.class);//new
            put("wf_pyramid", WiredAddonPyramid.class);//new

            put("bb_teleport", BanzaiTeleporterFloorItem.class);

            put("group_item", GroupFloorItem.class);

            put("football_timer", FootballTimerFloorItem.class);
            put("ball", FootballFloorItem.class);
            put("football_gate", FootballGateFloorItem.class);
            put("football_goal", FootballGoalFloorItem.class);
            put("football_score", FootballScoreFloorItem.class);
        }};
    }

    public static RoomItemFloor createFloor(int id, int baseId, Room room, int ownerId, int x, int y, double height, int rot, String data) {
        ItemDefinition def = CometManager.getItems().getDefinition(baseId);
        RoomItemFloor floorItem = null;

        if (def == null) {
            return null;
        }

        if (def.canSit) {
            return new SeatFloorItem(id, baseId, room, ownerId, x, y, height, rot, data);
        }

        if (def.getItemName().startsWith("tile_stackmagic")) {
            floorItem = new MagicStackFloorItem(id, baseId, room, ownerId, x, y, height, rot, data);
        }

        if (itemDefinitionMap.containsKey(def.getInteraction())) {
            try {
                floorItem = itemDefinitionMap.get(def.getInteraction()).getConstructor(int.class, int.class, Room.class, int.class, int.class, int.class, double.class, int.class, String.class)
                        .newInstance(id, baseId, room, ownerId, x, y, height, rot, data);
            } catch (Exception e) {
                log.warn("Failed to create instance for item: " + id + ", type: " + def.getInteraction());
                e.printStackTrace();
            }
        }

        if(floorItem == null) {
            floorItem = new GenericFloorItem(id, baseId, room, ownerId, x, y, height, rot, data);
        }

        return floorItem;
    }

    public static RoomItemWall createWall(int id, int baseId, Room room, int owner, String position, String data) {
        ItemDefinition def = CometManager.getItems().getDefinition(baseId);
        if (def == null) {
            return null;
        }

        switch (def.getInteraction()) {
            case "habbowheel": {
                return new WheelWallItem(id, baseId, room, owner, position, data);
            }
            case "dimmer": {
                return new MoodlightWallItem(id, baseId, room, owner, position, data);
            }
            default: {
                return new GenericWallItem(id, baseId, room, owner, position, data);
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
