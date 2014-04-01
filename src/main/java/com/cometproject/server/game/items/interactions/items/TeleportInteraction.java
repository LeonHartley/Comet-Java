/*package com.cometproject.server.game.items.interactions.items;

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
*/
package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.items.interactions.*;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.messenger.FollowFriendMessageComposer;

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

        item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 0, 1));
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
        FloorItem pairItem;
        int pairId;

        switch(updateState) {
            case 0: // init
                // user is initiating the teleport, open door and walk in
                // lock walking
                avatar.setIsInTeleporter(true);
                avatar.moveTo(item.getX(), item.getY());

                this.toggleDoor(item, true);

                item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 1, 6));
                break;

            case 1: // close door
                this.toggleDoor(item, false);

                item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 2, 5));
                break;

            case 2: // animate first portal
                this.toggleAnimation(item, true);

                item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 3, 8));
                break;

            case 3:
                pairId = GameEngine.getItems().getTeleportPartner(item.getId());
                pairItem = ((FloorItem)item).getRoom().getItems().getFloorItem(pairId);

                if(pairId == 0) {
                    item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 5, 5));
                    return false;
                }

                if(pairItem == null) {
                    int roomId = GameEngine.getItems().roomIdByItemId(pairId);

                    // if room exists, we visit it!
                    if(GameEngine.getRooms().get(roomId) != null) {
                        avatar.getPlayer().setTeleportId(pairId);
                        avatar.getPlayer().getSession().send(FollowFriendMessageComposer.compose(roomId));
                    }

                    item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 5, 5));
                    return false;
                }

                pairItem.queueInteraction(new InteractionQueueItem(true, pairItem, InteractionAction.ON_TICK, avatar, 4, 8));
                break;

            case 4: // stop first portal from animating and animate 2nd portal
                pairId = GameEngine.getItems().getTeleportPartner(item.getId());
                pairItem = ((FloorItem)item).getRoom().getItems().getFloorItem(pairId);

                if(pairItem != null) {
                    this.toggleAnimation(pairItem, false);
                }

                this.toggleAnimation(item, true);

                avatar.updateAndSetPosition(new Position3D(item.getX(), item.getY()));

                item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 5, 8));
                break;

            case 5: // stop portal animation
                this.toggleAnimation(item, false);
                item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 6, 5));
                break;

            case 6:
                this.toggleDoor(item, true);
                avatar.moveTo(item.squareInfront().getX(), item.squareInfront().getY());
                item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 7, 6));
                break;

            case 7:
                this.toggleDoor(item, false);
                // We're finished!

                avatar.setIsInTeleporter(false);
                break;
        }
        return false;
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