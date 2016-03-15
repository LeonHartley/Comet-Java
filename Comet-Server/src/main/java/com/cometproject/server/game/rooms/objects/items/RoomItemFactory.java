package com.cometproject.server.game.rooms.objects.items;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.rares.LimitedEditionItemData;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.DefaultWallItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.banzai.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.boutique.MannequinFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.football.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupGateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.hollywood.HaloTileFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.PetToyFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.horse.HorseJumpFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.snowboarding.SnowboardJumpFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.snowboarding.SnowboardSlopeFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.summer.SummerShowerFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.totem.TotemBodyFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.totem.TotemHeadFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.totem.TotemPlanetFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore.HighscoreClassicFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.*;
import com.cometproject.server.game.rooms.objects.items.types.wall.MoodlightWallItem;
import com.cometproject.server.game.rooms.objects.items.types.wall.PostItWallItem;
import com.cometproject.server.game.rooms.objects.items.types.wall.WheelWallItem;
import com.cometproject.server.game.rooms.types.Room;
import org.apache.log4j.Logger;

import java.util.HashMap;


public class RoomItemFactory {
    private static final int processMs = 500;
    private static final String GIFT_DATA = "GIFT::##";
    public static final String STACK_TOOL = "tile_stackmagic";
    public static final String TELEPORT_PAD = "teleport_pad";

    private static final Logger log = Logger.getLogger(RoomItemFactory.class.getName());

    private static final HashMap<String, Class<? extends RoomItemFloor>> itemDefinitionMap;

    static {
        itemDefinitionMap = new HashMap<String, Class<? extends RoomItemFloor>>() {{
            put("roller", RollerFloorItem.class);
            put("dice", DiceFloorItem.class);
            put("teleport", TeleporterFloorItem.class);
            put("teleport_door", TeleporterFloorItem.class);
            put("teleport_pad", TeleportPadFloorItem.class);
            put("onewaygate", OneWayGateFloorItem.class);
            put("gate", GateFloorItem.class);
            put("roombg", BackgroundTonerFloorItem.class);
            put("bed", BedFloorItem.class);
            put("vendingmachine", VendingMachineFloorItem.class);
            put("mannequin", MannequinFloorItem.class);
            put("beach_shower", SummerShowerFloorItem.class);
            put("halo_tile", HaloTileFloorItem.class);
            put("adjustable_height_seat", AdjustableHeightSeatFloorItem.class);
            put("adjustable_height", AdjustableHeightFloorItem.class);
            put("lovelock", LoveLockFloorItem.class);
            put("soundmachine", SoundMachineFloorItem.class);
            put("privatechat", PrivateChatFloorItem.class);

//            put("wf_act_flee", WiredActionFlee.class);//todo: this
            put("wf_act_match_to_sshot", WiredActionMatchToSnapshot.class);//new
            put("wf_act_teleport_to", WiredActionTeleportPlayer.class);//new
            put("wf_act_show_message", WiredActionShowMessage.class);//new
            put("wf_act_toggle_state", WiredActionToggleState.class);//new
            put("wf_act_give_reward", WiredActionGiveReward.class);//new
            put("wf_act_move_rotate", WiredActionMoveRotate.class);//new
            put("wf_act_chase", WiredActionChase.class);//new
            put("wf_act_kick_user", WiredActionKickUser.class);//new
            put("wf_act_reset_timers", WiredActionResetTimers.class);//new
            put("wf_act_join_team", WiredActionJoinTeam.class);//new
            put("wf_act_leave_team", WiredActionLeaveTeam.class);//new
            put("wf_act_give_score", WiredActionGiveScore.class);//new
            put("wf_act_bot_talk", WiredActionBotTalk.class);//new
            put("wf_act_bot_give_handitem", WiredActionBotGiveHandItem.class);//new
            put("wf_act_bot_move", WiredActionBotMove.class);//new
            put("wf_act_comet", WiredActionComet.class);//new
            put("wf_act_move_to_dir", WiredActionMoveToDirection.class);//new

            put("wf_trg_says_something", WiredTriggerPlayerSaysKeyword.class);//new
            put("wf_trg_enter_room", WiredTriggerEnterRoom.class);//new
            put("wf_trg_periodically", WiredTriggerPeriodically.class);//new
            put("wf_trg_walks_off_furni", WiredTriggerWalksOffFurni.class);//new
            put("wf_trg_walks_on_furni", WiredTriggerWalksOnFurni.class);//new
            put("wf_trg_state_changed", WiredTriggerStateChanged.class);//new
            put("wf_trg_game_starts", WiredTriggerGameStarts.class);//new
            put("wf_trg_game_ends", WiredTriggerGameEnds.class);//new
            put("wf_trg_collision", WiredTriggerCollision.class);//new
            put("wf_trg_period_long", WiredTriggerPeriodicallyLong.class);//new
            put("wf_trg_at_given_time", WiredTriggerAtGivenTime.class);//new
            put("wf_trg_at_given_time_long", WiredTriggerAtGivenTimeLong.class);//new
            put("wf_trg_score_achieved", WiredTriggerScoreAchieved.class);//new

            put("wf_cnd_trggrer_on_frn", WiredConditionTriggererOnFurni.class);//new
            put("wf_cnd_not_trggrer_on", WiredNegativeConditionTriggererOnFurni.class);//new
            put("wf_cnd_actor_in_group", WiredConditionPlayerInGroup.class);//new
            put("wf_cnd_not_in_group", WiredNegativeConditionPlayerInGroup.class);//new
            put("wf_cnd_furnis_hv_avtrs", WiredConditionFurniHasPlayers.class);//new
            put("wf_cnd_not_hv_avtrs", WiredNegativeConditionFurniHasPlayers.class);//new
            put("wf_cnd_wearing_badge", WiredConditionPlayerHasBadgeEquipped.class);//new
            put("wf_cnd_not_wearing_badge", WiredNegativeConditionPlayerHasBadgeEquipped.class);//new
            put("wf_cnd_wearing_effect", WiredConditionPlayerWearingEffect.class);//new
            put("wf_cnd_not_wearing_effect", WiredNegativeConditionPlayerWearingEffect.class);//new
            put("wf_cnd_has_furni_on", WiredConditionHasFurniOn.class);//new
            put("wf_cnd_not_furni_on", WiredNegativeConditionHasFurniOn.class);//new
            put("wf_cnd_user_count_in", WiredConditionPlayerCountInRoom.class);//new
            put("wf_cnd_not_user_count", WiredConditionPlayerCountInRoom.class);//new
            put("wf_cnd_match_snapshot", WiredConditionMatchSnapshot.class);//new
            put("wf_cnd_not_match_snap", WiredNegativeConditionMatchSnapshot.class);//new
            put("wf_cnd_has_handitem", WiredConditionHasHandItem.class);//new
            put("wf_cnd_time_more_than", WiredConditionTimeMoreThan.class);//new
            put("wf_cnd_time_less_than", WiredConditionTimeLessThan.class);//new

            put("wf_xtra_random", WiredAddonRandomEffect.class);
            put("wf_xtra_unseen", WiredAddonUnseenEffect.class);

            put("wf_floor_switch1", WiredAddonFloorSwitch.class);//new
            put("wf_floor_switch2", WiredAddonFloorSwitch.class);//new
            put("wf_colorwheel", WiredAddonColourWheel.class);//new
            put("wf_pressureplate", WiredAddonPressurePlate.class);//new
            put("wf_arrowplate", WiredAddonPressurePlate.class);//new
            put("wf_ringplate", WiredAddonPressurePlate.class);//new
            put("wf_pyramid", WiredAddonPyramid.class);//new
            put("wf_visual_timer", WiredAddonVisualTimer.class);//new

            put("highscore_classic", HighscoreClassicFloorItem.class);

            put("pressureplate_seat", PressurePlateSeatFloorItem.class);

            put("bb_teleport", BanzaiTeleporterFloorItem.class);
            put("bb_red_gate", BanzaiGateFloorItem.class);
            put("bb_yellow_gate", BanzaiGateFloorItem.class);
            put("bb_blue_gate", BanzaiGateFloorItem.class);
            put("bb_green_gate", BanzaiGateFloorItem.class);
            put("bb_patch", BanzaiTileFloorItem.class);
//            put("bb_timer", BanzaiTimerFloorItem.class);
            put("bb_puck", BanzaiPuckFloorItem.class);

            put("group_item", GroupFloorItem.class);
            put("group_forum", GroupFloorItem.class);
            put("group_gate", GroupGateFloorItem.class);

            put("football_timer", FootballTimerFloorItem.class);
            put("ball", FootballFloorItem.class);
            put("football_gate", FootballGateFloorItem.class);
            put("football_goal", FootballGoalFloorItem.class);
            put("football_score", FootballScoreFloorItem.class);

            put("snowb_slope", SnowboardSlopeFloorItem.class);
            put("snowb_rail", SnowboardJumpFloorItem.class);
            put("snowb_jump", SnowboardJumpFloorItem.class);

            put("totem_planet", TotemPlanetFloorItem.class);
            put("totem_head", TotemHeadFloorItem.class);
            put("totem_body", TotemBodyFloorItem.class);

            put("pet_toy", PetToyFloorItem.class);

            put("cannon", CannonFloorItem.class);

            put("horse_jump", HorseJumpFloorItem.class);
        }};
    }

    public static RoomItemFloor createFloor(long id, int baseId, Room room, int ownerId, int x, int y, double height, int rot, String data, LimitedEditionItemData limitedEditionItemData) {
        ItemDefinition def = ItemManager.getInstance().getDefinition(baseId);
        RoomItemFloor floorItem = null;

        if (def == null) {
            return null;
        }

        if (def.canSit()) {
            floorItem = new SeatFloorItem(id, baseId, room, ownerId, x, y, height, rot, data);
        }

        if (def.getItemName().startsWith(STACK_TOOL)) {
            floorItem = new MagicStackFloorItem(id, baseId, room, ownerId, x, y, height, rot, data);
        }

        if (data.startsWith(GIFT_DATA)) {
            try {
                floorItem = new GiftFloorItem(id, baseId, room, ownerId, x, y, height, rot, data);
            } catch (Exception e) {
                return null;
//                floorItem = new DefaultFloorItem(id, baseId, room, ownerId, x, y, height, rot, "");
            }
        } else {
            if (itemDefinitionMap.containsKey(def.getInteraction())) {
                try {
                    floorItem = itemDefinitionMap.get(def.getInteraction()).getConstructor(long.class, int.class, Room.class, int.class, int.class, int.class, double.class, int.class, String.class)
                            .newInstance(id, baseId, room, ownerId, x, y, height, rot, data);
                } catch (Exception e) {
                    log.warn("Failed to create instance for item: " + id + ", type: " + def.getInteraction(), e);
                }
            }
        }

        if (floorItem == null) {
            floorItem = new DefaultFloorItem(id, baseId, room, ownerId, x, y, height, rot, data);
        }

        if (limitedEditionItemData != null) {
            floorItem.setLimitedEditionItemData(limitedEditionItemData);
        }

        return floorItem;
    }

    public static RoomItemWall createWall(long id, int baseId, Room room, int owner, String position, String data, LimitedEditionItemData limitedEditionItemData) {
        ItemDefinition def = ItemManager.getInstance().getDefinition(baseId);
        if (def == null) {
            return null;
        }

        RoomItemWall wallItem;

        switch (def.getInteraction()) {
            case "habbowheel": {
                wallItem = new WheelWallItem(id, baseId, room, owner, position, data);
                break;
            }
            case "dimmer": {
                wallItem = new MoodlightWallItem(id, baseId, room, owner, position, data);
                break;
            }
            case "postit": {
                wallItem = new PostItWallItem(id, baseId, room, owner, position, data);
                break;
            }
            default: {
                wallItem = new DefaultWallItem(id, baseId, room, owner, position, data);
                break;
            }
        }

        if (limitedEditionItemData != null) {
            wallItem.setLimitedEditionItemData(limitedEditionItemData);
        }

        return wallItem;
    }

    public static int getProcessTime(double time) {
        long realTime = Math.round(time * 1000 / processMs);

        if (realTime < 1) {
            realTime = 1; //0.5s
        }

        return (int) realTime;
    }
}
