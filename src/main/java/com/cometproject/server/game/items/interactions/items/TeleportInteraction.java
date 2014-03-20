package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.items.interactions.*;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;

public class TeleportInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar, boolean isWiredTriggered) {
        Position3D posInFront = item.squareInfront();

        if((avatar.getPosition().getX() != posInFront.getX() && avatar.getPosition().getY() != posInFront.getY())
                && !(avatar.getPosition().getX() == item.getX() && avatar.getPosition().getY() == item.getY())) {
            avatar.moveTo(posInFront.getX(), posInFront.getY());

            return false;
        }

        avatar.getPlayer().getSession().send(AdvancedAlertMessageComposer.compose("Feature Disabled", "This feature is currently disabled."));

        //item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 0, 1));
        return false;
    }

    @Override
    public boolean onPlace(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(RoomItem item, PlayerEntity avatar, int updateState) {
       // TeleportAction action = TeleportAction.values()[updateState];

       // switch(action) {
       //     case OPEN_DOOR:
       //
       //         break;
       // }
        return false;
    }


    public enum TeleportAction {
        OPEN_DOOR (0),
        CLOSE_DOOR (1),
        ANIMATION_ON (2),
        ANIMATION_OFF (3),
        TELEPORT_USER (4),
        WALK_OUT (5),
        WALK_IN (6);

        private int value;

        TeleportAction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public void toggleDoor(RoomItem item, boolean state) {
        if(state)
            item.setExtraData("1");
        else
            item.setExtraData("0");

        item.sendUpdate();
    }

    public void toggleAnimation(RoomItem item, boolean state) {
        if(state)
            item.setExtraData("2");
        else
            item.setExtraData("0");

        item.sendUpdate();
    }
    @Override
    public boolean requiresRights() {
        return false;
    }
}
