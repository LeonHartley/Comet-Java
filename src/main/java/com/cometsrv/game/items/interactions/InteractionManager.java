package com.cometsrv.game.items.interactions;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.items.interactions.banzai.BanzaiPatchInteraction;
import com.cometsrv.game.items.interactions.banzai.gates.BanzaiGateBlueInteraction;
import com.cometsrv.game.items.interactions.banzai.gates.BanzaiGateGreenInteraction;
import com.cometsrv.game.items.interactions.banzai.gates.BanzaiGateRedInteraction;
import com.cometsrv.game.items.interactions.banzai.gates.BanzaiGateYellowInteraction;
import com.cometsrv.game.items.interactions.items.*;
import com.cometsrv.game.items.interactions.wired.action.WiredActionMoveRotate;
import com.cometsrv.game.items.interactions.wired.action.WiredActionMoveUser;
import com.cometsrv.game.items.interactions.wired.action.WiredActionShowMessage;
import com.cometsrv.game.items.interactions.wired.trigger.WiredTriggerEnterRoom;
import com.cometsrv.game.items.interactions.wired.trigger.WiredTriggerOnSay;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import javolution.util.FastMap;

import java.util.Map;

public class InteractionManager {
    public static final int DICE_ROLL_TIME = Integer.parseInt(Comet.getServer().getConfig().get("comet.game.interactions.dice.cycles"));

    private FastMap<String, Interactor> interactions;

    public InteractionManager() {
        this.interactions = new FastMap<>();
        this.loadInteractions();
    }

    public void loadInteractions() {
        // Furniture
        this.interactions.put("dice", new DiceInteraction());
        this.interactions.put("default", new DefaultInteraction());
        this.interactions.put("gate", new GateInteraction());
        this.interactions.put("pressure_pad", new PressurePadInteraction());
        this.interactions.put("teleport", new TeleportInteraction());

        // Wired Actions
        this.interactions.put("wf_act_move_rotate", new WiredActionMoveRotate());
        this.interactions.put("wf_act_saymsg", new WiredActionShowMessage());
        this.interactions.put("wf_act_moveuser", new WiredActionMoveUser());

        // Wired Triggers
        this.interactions.put("wf_trg_onsay", new WiredTriggerOnSay());
        this.interactions.put("wf_trg_enterroom", new WiredTriggerEnterRoom());

        // Wired Conditions

        // Banzai
        this.interactions.put("bb_patch", new BanzaiPatchInteraction());
        this.interactions.put("bb_green_gate", new BanzaiGateGreenInteraction());
        this.interactions.put("bb_yellow_gate", new BanzaiGateYellowInteraction());
        this.interactions.put("bb_blue_gate", new BanzaiGateBlueInteraction());
        this.interactions.put("bb_red_gate", new BanzaiGateRedInteraction());
    }

    public InteractionState onWalk(boolean state, FloorItem item, Avatar avatar) {
        if (!this.isInteraction(item.getDefinition().getInteraction())) {
            return InteractionState.NO_INTERACTION;
        }

        if (this.getInteractions().get(item.getDefinition().getInteraction()).onWalk(state, item, avatar)) {
            return InteractionState.COMPLETED;
        }

        return InteractionState.NO_INTERACTION;
    }

    public InteractionState onInteract(int state, FloorItem item, Avatar avatar) {
        GameEngine.getLogger().debug("Interacted with: " + item.getDefinition().getInteraction());

        if(!this.isInteraction(item.getDefinition().getInteraction())) {
            return InteractionState.NO_INTERACTION;
        }

        Interactor action = this.getInteractions().get(item.getDefinition().getInteraction());

        if(action.requiresRights() && !avatar.getRoom().getRights().hasRights(avatar.getPlayer().getId()))
            return InteractionState.NO_INTERACTION;

        if (action.onInteract(state, item, avatar)) {
            return InteractionState.COMPLETED;
        }

        return InteractionState.NO_INTERACTION;
    }

    // Method not yet finished!
    public InteractionState onPlace(FloorItem item, Avatar avatar, Room room) {
        return InteractionState.NO_INTERACTION;
    }

    // Method not yet finished!
    public InteractionState onPickup(FloorItem item, Avatar avatar, Room room) {
        return InteractionState.NO_INTERACTION;
    }

    public InteractionState onTick(FloorItem item) {
        GameEngine.getLogger().debug("Item tick: " + item.getDefinition().getInteraction());

        if (!this.isInteraction(item.getDefinition().getInteraction())) {
            return InteractionState.NO_INTERACTION;
        }

        if (item.needsCycling()) {
            return InteractionState.CYCLING;
        }

        Interactor action = this.getInteractions().get(item.getDefinition().getInteraction());

        if (action.onTick(item)) {
            return InteractionState.COMPLETED;
        }

        return InteractionState.NO_INTERACTION;
    }

    private boolean isInteraction(String interaction) {
        return this.getInteractions().containsKey(interaction);
    }

    public Map<String, Interactor> getInteractions() {
        return this.interactions;
    }
}
