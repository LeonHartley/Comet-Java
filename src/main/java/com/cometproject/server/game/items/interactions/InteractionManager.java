package com.cometproject.server.game.items.interactions;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.items.interactions.banzai.*;
import com.cometproject.server.game.items.interactions.football.BallInteraction;
import com.cometproject.server.game.items.interactions.items.*;
import com.cometproject.server.game.items.interactions.wired.action.WiredActionMoveRotate;
import com.cometproject.server.game.items.interactions.wired.action.WiredActionMoveUser;
import com.cometproject.server.game.items.interactions.wired.action.WiredActionShowMessage;
import com.cometproject.server.game.items.interactions.wired.action.WiredActionToggleFurni;
import com.cometproject.server.game.items.interactions.wired.trigger.*;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import javolution.util.FastMap;

import java.util.Map;

public class InteractionManager {
    public static final boolean DICE_ENABLED = Integer.parseInt(Comet.getServer().getConfig().get("comet.game.interactions.dice.enabled")) == 1;
    public static final int DICE_ROLL_TIME = Integer.parseInt(Comet.getServer().getConfig().get("comet.game.interactions.dice.cycles"));

    private FastMap<String, Interactor> interactions;

    public InteractionManager() {
        this.interactions = new FastMap<>();
        this.loadInteractions();
    }

    public void loadInteractions() {
        // Furniture
        this.interactions.put("default", new DefaultInteraction());
        this.interactions.put("gate", new GateInteraction());
        this.interactions.put("pressure_pad", new PressurePadInteraction());
        this.interactions.put("teleport", new TeleportInteraction());
        this.interactions.put("habbowheel", new WheelInteraction());
        this.interactions.put("roller", new RollerInteraction());
        this.interactions.put("ball", new BallInteraction());
        this.interactions.put("mannequin", new MannequinInteraction());
        this.interactions.put("roombg", new BackgroundTonerInteraction());
        this.interactions.put("vendingmachine", new VendingMachineInteraction());
        this.interactions.put("onewaygate", new OneWayGateInteraction());
        this.interactions.put("puzzlebox", new PuzzleBoxInteraction());

        if (InteractionManager.DICE_ENABLED) {
            this.interactions.put("dice", new DiceInteraction());
        }

        // Wired Actions
        this.interactions.put("wf_act_moverotate", new WiredActionMoveRotate());
        this.interactions.put("wf_act_saymsg", new WiredActionShowMessage());
        this.interactions.put("wf_act_moveuser", new WiredActionMoveUser());
        this.interactions.put("wf_act_togglefurni", new WiredActionToggleFurni());

        // Wired Triggers
        this.interactions.put("wf_trg_onsay", new WiredTriggerOnSay());
        this.interactions.put("wf_trg_enterroom", new WiredTriggerEnterRoom());
        this.interactions.put("wf_trg_onfurni", new WiredTriggerOnFurni());
        this.interactions.put("wf_trg_offfurni", new WiredTriggerOffFurni());
        this.interactions.put("wf_trg_timer", new WiredTriggerTimer());

        // Wired Conditions

        // Banzai
        this.interactions.put("bb_patch", new BanzaiPatchInteraction());
        this.interactions.put("bb_green_gate", new BanzaiGateInteraction());
        this.interactions.put("bb_yellow_gate", new BanzaiGateInteraction());
        this.interactions.put("bb_blue_gate", new BanzaiGateInteraction());
        this.interactions.put("bb_red_gate", new BanzaiGateInteraction());
        this.interactions.put("bb_teleport", new BanzaiTeleportInteraction());
        this.interactions.put("bb_timer", new BanzaiTimerInteraction());
        this.interactions.put("bb_score_b", new BanzaiScoreInteraction());
        this.interactions.put("bb_score_r", new BanzaiScoreInteraction());
        this.interactions.put("bb_score_g", new BanzaiScoreInteraction());
        this.interactions.put("bb_score_y", new BanzaiScoreInteraction());
    }

    public void onWalk(boolean state, FloorItem item, GenericEntity avatar) {
        if (this.isInteraction(item.getDefinition().getInteraction())) {
            if (this.getInteractions().get(item.getDefinition().getInteraction()).onWalk(state, item, avatar)) {
                // ??
            }
        }
    }

    public void onPreWalk(FloorItem item, GenericEntity avatar) {
        if (this.isInteraction(item.getDefinition().getInteraction())) {
            if (this.getInteractions().get(item.getDefinition().getInteraction()).onPreWalk(item, avatar)) {
                // ??
            }
        }
    }

    public void onInteract(int state, RoomItem item, GenericEntity avatar, boolean isWiredTriggered) {
        CometManager.getLogger().debug("Interacted with: " + item.getDefinition().getInteraction());

        if (!this.isInteraction(item.getDefinition().getInteraction())) {
            return;
        }

        Interactor action = this.getInteractions().get(item.getDefinition().getInteraction());

        if (!isWiredTriggered) {
            if(avatar instanceof PlayerEntity) {
                if (action.requiresRights() && (!avatar.getRoom().getRights().hasRights(((PlayerEntity) avatar).getPlayer().getId()) && !((PlayerEntity) avatar).getPlayer().getPermissions().hasPermission("room_full_control"))) {
                    return;
                }
            }
        }

        if (action.onInteract(state, item, avatar, isWiredTriggered)) {
            // ??
        }
    }

    public void onInteract(int state, RoomItem item, GenericEntity entity) {
        this.onInteract(state, item, entity, false);
    }

    // Method not yet finished!
    public void onPlace(FloorItem item, PlayerEntity avatar, Room room) {

    }

    // Method not yet finished!
    public void onPickup(FloorItem item, PlayerEntity avatar, Room room) {

    }

    public void onTick(RoomItem item) {
        /*CometManager.getLogger().debug("GenericRoomItem tick: " + item.getDefinition().getInteraction());

        if (!this.isInteraction(item.getDefinition().getInteraction())) {
            return;
        }

        InteractionQueueItem interactionQueueItem = item.getNextInteraction();

        if (interactionQueueItem == null || interactionQueueItem.needsCycling()) {
            return;
        }

        Interactor action = this.getInteractions().get(item.getDefinition().getInteraction());

        if (action.onTick(item, interactionQueueItem.getEntity(), interactionQueueItem.getUpdateState())) {
            // ??
        }*/
    }

    private boolean isInteraction(String interaction) {
        return this.getInteractions().containsKey(interaction);
    }

    public Map<String, Interactor> getInteractions() {
        return this.interactions;
    }
}
