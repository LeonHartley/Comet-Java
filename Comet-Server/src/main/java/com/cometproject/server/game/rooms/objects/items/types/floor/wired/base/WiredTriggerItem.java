package com.cometproject.server.game.rooms.objects.items.types.floor.wired.base;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.WiredActionExecuteStacks;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.WiredActionKickUser;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons.WiredAddonUnseenEffect;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative.WiredNegativeConditionHasFurniOn;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative.WiredNegativeConditionTriggererOnFurni;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.WiredConditionHasFurniOn;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.WiredConditionTriggererOnFurni;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerEnterRoom;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.wired.dialog.WiredTriggerMessageComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.utilities.RandomUtil;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class WiredTriggerItem extends WiredFloorItem {
    private static Logger log = Logger.getLogger(WiredTriggerItem.class.getName());

    public WiredTriggerItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    public static <T extends RoomItemFloor> List<T> getTriggers(Room room, Class<T> clazz) {
        final List<T> triggers = Lists.newArrayList();

        for (RoomItemFloor floorItem : room.getItems().getByClass(clazz)) {
            if (triggers.size() <= CometSettings.wiredMaxTriggers)
                triggers.add((T) floorItem);
        }

        return triggers;
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        try {
            // if the trigger relies on an entity being provided and there wasn't one, ignore.
            if (this.suppliesPlayer() && entity == null) {
                return false;
            }

            // create empty list for all wired actions on the instance tile
            List<WiredActionItem> wiredActions = Lists.newArrayList();

            // create empty list for all wired conditions on instance tile
            List<WiredConditionItem> wiredConditions = Lists.newArrayList();

            // flood protection regarding wt_act_execute_stacks
            int executeStacksCount = 0;

            // used by addons
            WiredAddonUnseenEffect unseenEffectItem = null;

            boolean canExecute = true;

            // Wired animation
            this.flash();

            // loop through all items on this tile
            for (RoomItemFloor floorItem : this.getItemsOnStack()) {
                if (floorItem instanceof WiredActionItem && wiredActions.size() <= CometSettings.wiredMaxEffects) {
                    // protect against mass usage of wired to cause lag & crashes.
                    if (floorItem instanceof WiredActionExecuteStacks) {
                        if (executeStacksCount >= CometSettings.wiredMaxExecuteStacks) {
                            continue;
                        }

                        executeStacksCount++;
                    }

                    // if the item is a wired action, add it to the list of actions
                    wiredActions.add(((WiredActionItem) floorItem));
                } else if (floorItem instanceof WiredConditionItem) {

                    // if the item is a wired condition, add it to the list of conditions
                    wiredConditions.add((WiredConditionItem) floorItem);
                } else if (floorItem instanceof WiredAddonUnseenEffect && unseenEffectItem == null) {

                    unseenEffectItem = ((WiredAddonUnseenEffect) floorItem);
                }
            }

            if (unseenEffectItem != null && unseenEffectItem.getSeenEffects().size() >= wiredActions.size()) {
                unseenEffectItem.getSeenEffects().clear();
            }

            final Map<WiredConditionItem, AtomicBoolean> completedConditions = new HashMap<>();

            // loop through the conditions and check whether or not we can perform the action
            for (WiredConditionItem conditionItem : wiredConditions) {
                conditionItem.flash();

                if (!completedConditions.containsKey(conditionItem)) {
                    completedConditions.put(conditionItem, new AtomicBoolean(false));
                }

                completedConditions.get(conditionItem).set(conditionItem.evaluate(entity, data));
            }

            boolean hasSuccessfulOnStack = false;
            boolean hasSuccessfulOnFurni = false;

            for (Map.Entry<WiredConditionItem, AtomicBoolean> conditionState : completedConditions.entrySet()) {
                if (conditionState.getKey() instanceof WiredConditionHasFurniOn && !(conditionState.getKey() instanceof WiredNegativeConditionHasFurniOn)) {
//                    final WiredConditionHasFurniOn conditionHasFurniOn = (WiredConditionHasFurniOn) conditionState.getKey();

                    if (conditionState.getValue().get()) {
                        hasSuccessfulOnStack = true;
                    } else {
                        if (!hasSuccessfulOnStack) {
                            canExecute = false;
                        }
                    }

                    continue;
                }

                if (conditionState.getKey() instanceof WiredConditionTriggererOnFurni &&
                        !(conditionState.getKey() instanceof WiredNegativeConditionTriggererOnFurni)) {
                    if (conditionState.getValue().get()) {
                        hasSuccessfulOnFurni = true;
                    } else {
                        if (!hasSuccessfulOnFurni) {
                            canExecute = false;
                        }
                    }
                } else {
                    if (!conditionState.getValue().get()) {
                        canExecute = false;
                    }
                }
            }

            if (hasSuccessfulOnFurni || hasSuccessfulOnStack) {
                canExecute = true;
            }

            // tell the trigger that the item can execute, but hasn't executed just yet!
            // (just incase you wanna cancel the event that triggered this or do something else... who knows?!?!)
            this.preActionTrigger(entity, data);

            // if we can perform the action, let's perform it!
            if (canExecute && wiredActions.size() >= 1) {
                // if the execution was a success, this will be set to true and returned so that the
                // event that called this wired trigger can do what it needs to do
                boolean wasSuccess = false;

                if (unseenEffectItem != null) {
                    for (WiredActionItem actionItem : wiredActions) {
                        if (!unseenEffectItem.getSeenEffects().contains(actionItem.getId())) {
                            unseenEffectItem.getSeenEffects().add(actionItem.getId());

                            if (this.executeEffect(actionItem, entity, data))
                                wasSuccess = true;
                            break;
                        }
                    }

                    return wasSuccess;
                } else {
                    for (WiredActionItem actionItem : wiredActions) {
                        if (this.executeEffect(actionItem, entity, data)) {
                            wasSuccess = true;
                        }
                    }
                }

                return wasSuccess;
            }
        } catch (Exception e) {
            log.error("Error during WiredTrigger evaluation", e);
        }

        // tell the event that called the trigger that it was not a success!
        return false;
    }

    private boolean executeEffect(WiredActionItem actionItem, RoomEntity entity, Object data) {
        if (this instanceof WiredTriggerEnterRoom && actionItem instanceof WiredActionKickUser) {
            if (entity != null) {
                if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getPlayer() != null) {
                    if (!((PlayerEntity) entity).getPlayer().getPermissions().getRank().roomKickable()) {
                        return false;
                    }
                }
            }
        }

        return actionItem.evaluate(entity, data);
    }

    @Override
    public MessageComposer getDialog() {
        return new WiredTriggerMessageComposer(this);
    }

    public List<WiredActionItem> getIncompatibleActions() {
        // create an empty list to add the incompatible actions
        List<WiredActionItem> incompatibleActions = Lists.newArrayList();

        // check whether or not this instance trigger supplies a player
        if (!this.suppliesPlayer()) {
            // if it doesn't, loop through all items on instance tile
            for (RoomItemFloor floorItem : this.getItemsOnStack()) {
                if (floorItem instanceof WiredActionItem) {
                    // check whether the item needs a player to perform its action
                    if (((WiredActionItem) floorItem).requiresPlayer()) {
                        // if it does, add it to the incompatible actions list!
                        incompatibleActions.add(((WiredActionItem) floorItem));
                    }
                }
            }
        }

        return incompatibleActions;
    }

    public void preActionTrigger(RoomEntity entity, Object data) {
        // override me if u want to!!!!111one
    }

    public abstract boolean suppliesPlayer();


}
