package com.cometproject.server.game.rooms.objects.items.types.floor.wired.base;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.AbstractWiredItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.wired.dialog.WiredTriggerMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.google.common.collect.Lists;

import java.util.List;

public abstract class WiredTriggerItem extends AbstractWiredItem {
    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredTriggerItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean evaluate(GenericEntity entity, Object data) {
        // create empty list for all wired actions on the current tile
        List<WiredActionItem> wiredActions = Lists.newArrayList();

        // create empty list for all wired conditions on current tile
        List<WiredConditionItem> wiredConditions = Lists.newArrayList();

        boolean canExecute = true;

        // loop through all items on this tile
        for (RoomItemFloor floorItem : this.getItemsOnStack()) {
            if (floorItem instanceof WiredActionItem) {
                // if the item is a wired action, add it to the list of actions
                wiredActions.add(((WiredActionItem) floorItem));
            } else if (floorItem instanceof WiredConditionItem) {

                // if the item is a wired condition, add it to the list of conditions
                wiredConditions.add((WiredConditionItem) floorItem);
            }
        }

        // loop through the conditions and check whether or not we can perform the action
        for (WiredConditionItem conditionItem : wiredConditions) {
            if (!conditionItem.evaluate(entity, data)) {
                canExecute = false;
            }
        }

        // tell the trigger that the item can execute, but hasn't executed just yet!
        // (just incase you wanna cancel the event that triggered this or do something else... who knows?!?!)
        this.preActionTrigger(entity, data);

        // if we can perform the action, let's perform it!
        if (canExecute) {
            // if the execution was a success, this will be set to true and returned so that the
            // event that called this wired trigger can do what it needs to do
            boolean wasSuccess = false;

            for (WiredActionItem actionItem : wiredActions) {
                if(actionItem.evaluate(entity, data)) {
                    wasSuccess = true;
                }
            }

            return wasSuccess;
        }

        // tell the event that called the trigger that it was not a success!
        return false;
    }

    @Override
    public Composer getDialog() {
        return WiredTriggerMessageComposer.compose(this);
    }

    public List<WiredActionItem> getIncompatibleActions() {
        // create an empty list to add the incompatible actions
        List<WiredActionItem> incompatibleActions = Lists.newArrayList();

        // check whether or not this current trigger supplies a player
        if (!this.suppliesPlayer()) {
            // if it doesn't, loop through all items on current tile
            for(RoomItemFloor floorItem : this.getItemsOnStack()) {
                if(floorItem instanceof WiredActionItem) {
                    // check whether the item needs a player to perform its action
                    if(((WiredActionItem) floorItem).requiresPlayer()) {
                        // if it does, add it to the incompatible actions list!
                        incompatibleActions.add(((WiredActionItem) floorItem));
                    }
                }
            }
        }

        return incompatibleActions;
    }

    public void preActionTrigger(GenericEntity entity, Object data) {
        // override me if u want to!!!!111one
    }

    public abstract boolean suppliesPlayer();


}