package com.cometsrv.game.rooms.entities.types;

import com.cometsrv.config.CometSettings;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.players.types.Player;
import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.avatars.pathfinding.Square;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.entities.PlayerEntityAccess;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.components.types.Trade;
import com.cometsrv.game.wired.types.TriggerType;
import com.cometsrv.network.messages.outgoing.room.alerts.RoomFullMessageComposer;
import com.cometsrv.network.messages.outgoing.room.avatar.IdleStatusMessageComposer;
import com.cometsrv.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometsrv.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometsrv.network.messages.outgoing.room.engine.ModelAndIdMessageComposer;
import com.cometsrv.network.messages.outgoing.room.engine.PapersMessageComposer;
import com.cometsrv.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometsrv.network.messages.outgoing.room.permissions.FloodFilterMessageComposer;
import com.cometsrv.network.messages.outgoing.room.permissions.OwnerRightsMessageComposer;
import com.cometsrv.network.messages.types.Composer;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerEntity extends GenericEntity implements PlayerEntityAccess {
    private Player player;

    public PlayerEntity(Player player, int identifier, Position3D startPosition, int startBodyRotation, int startHeadRotation, Room roomInstance) {
        super(identifier, startPosition, startBodyRotation, startHeadRotation, roomInstance);

        this.player = player;
    }

    @Override
    public void joinRoom(Room room, String password) {
        if (this.getRoom() == null) {
            this.player.getSession().send(HotelViewMessageComposer.compose());
            return;
        }

        // Is the room loaded
        // Method has been moved to initialize room packet.
        /*if (!this.getRoom().isActive) {
            this.getRoom().load();
        }*/

        // Room full or slot available
        if (this.getRoom().getEntities().count() >= this.getRoom().getData().getMaxUsers() && !this.player.getPermissions().hasPermission("room_enter_full")) {
            this.player.getSession().send(RoomFullMessageComposer.compose());
            this.player.getSession().send(HotelViewMessageComposer.compose());
            return;
        }

        // Room bans
        if (this.getRoom().getRights().hasBan(this.player.getId()) && !this.player.getPermissions().hasPermission("room_unkickable")) {
            // TODO: Proper ban message
            this.player.getSession().send(HotelViewMessageComposer.compose());
        }

        boolean isOwner = this.getRoom().getData().getOwnerId() == this.player.getId();

        if (!isOwner && !this.player.getPermissions().hasPermission("room_enter_locked")) {
            if (this.getRoom().getData().getAccess().equals("password") && !this.getRoom().getData().getPassword().equals(password)) {
                // TODO: Invalid password message
                this.player.getSession().send(HotelViewMessageComposer.compose());
                return;
            } else if (this.getRoom().getData().getAccess().equals("doorbell")) {
                // TODO: Doorbell
                this.player.getSession().send(HotelViewMessageComposer.compose());
                return;
            }
        }

        //this.authPassed = true;

        this.finalizeJoinRoom();
    }

    @Override
    protected void finalizeJoinRoom() {
        this.player.getSession().send(ModelAndIdMessageComposer.compose(this.getRoom().getModel().getId(), this.getVirtualId()));

        // Wallpaper and floor decorations
        for (String decoration : this.getRoom().getData().getDecorations()) {
            String[] deco = decoration.split("=");

            if(deco[0].equals("wallpaper") || deco[0].equals("floor")) {
                if(deco[1].equals("0.0")) {
                    continue;
                }
            }

            this.player.getSession().send(PapersMessageComposer.compose(deco[0], deco[1]));
        }

        int accessLevel = 0;

        if (this.getRoom().getData().getOwnerId() == this.player.getId() || this.player.getPermissions().hasPermission("full_room_access")) {
            this.addStatus("flatctrl 4", "useradmin");
            accessLevel = 4;
        } else if (this.getRoom().getRights().hasRights(this.player.getId())) {
            this.addStatus("flatctrl 1", "");
            accessLevel = 1;
        }

        this.player.getSession().send(AccessLevelMessageComposer.compose(accessLevel));

        if (this.getRoom().getData().getOwnerId() == this.player.getId()) {
            this.player.getSession().send(OwnerRightsMessageComposer.compose());
        }
    }

    @Override
    public void leaveRoom(boolean isOffline, boolean isKick, boolean toHotelView) {
        // Clear all  statuses
        this.getStatuses().clear();

        // Send leave room message to all current entities
        this.getRoom().getEntities().broadcastMessage(LeaveRoomMessageComposer.compose(this.getVirtualId()));

        // Sending this user to the hotel view?
        if (!isOffline && toHotelView) {
            this.getPlayer().getSession().send(HotelViewMessageComposer.compose());
        }

        // TODO: Change trade from 'Session' to 'PlayerEntity' to avoid confusion between player and virtual id
        // Also could be useful for bot trading etc

        // Check and cancel any active trades
        Trade trade = this.getRoom().getTrade().get(this.getPlayer().getSession());

        if (trade != null) {
            trade.cancel(this.getPlayer().getId());
        }

        // Remove entity from the room
        this.getRoom().getEntities().removeEntity(this);

        // De-reference things
        this.getPlayer().setAvatar(null);
        this.player = null;
    }

    @Override
    public void finalizeLeaveRoom() {
        // not used, could be removed?
    }

    @Override
    public boolean onChat(String message) {
        long time = System.currentTimeMillis();

        if (time - this.player.lastMessage < 500) {
            this.player.floodFlag++;

            if(this.player.floodFlag >= 4) {
                this.player.floodTime = 30;
                this.player.floodFlag = 0;

                this.player.getSession().send(FloodFilterMessageComposer.compose(player.floodTime));
            }
        }

        if (player.floodTime >= 1) {
            return false;
        }

        player.lastMessage = time;

        try {
            if(message.startsWith(":")) {
                if(GameEngine.getCommands().isCommand(message.substring(1))) {
                    GameEngine.getCommands().parse(message.substring(1), this.player.getSession());
                    return false;
                }
            }
        } catch (Exception e) {
            // command error?
        }

        if(this.getRoom().getWired().trigger(TriggerType.ON_SAY, message, this)) {
            return false;
        }

        if(CometSettings.logChatToConsole) {
            this.getRoom().log.info(this.getPlayer().getData().getUsername() + ": " + message);
        }

        this.getRoom().getChatlog().add(message, this.getPlayer().getId());

        this.unIdle();
        return true;
    }

    public void moveTo(int x, int y) {
        // TODO: Redirection grid here for beds

        if (this.getPositionToSet() != null){
            this.setPosition(this.getPositionToSet());
        }

        // Set the goal we are wanting to achieve
        this.setWalkingGoal(x, y);

        // Create a walking path
        LinkedList<Square> path = this.getPathfinder().makePath();

        // Check returned path to see if it calculated one
        if (path == null || path.size() == 0) {
            // Reset the goal and return as no path was found
            this.setWalkingGoal(this.getPosition().getX(), this.getPosition().getY());
            return;
        }

        // UnIdle the user and set the path (if the path has nodes it will mean the user is walking)
        this.unIdle();
        this.setWalkingPath(path);
    }

    @Override
    public void setIdle() {
        super.setIdle();

        this.getRoom().getEntities().broadcastMessage(IdleStatusMessageComposer.compose(this.player.getId(), true));
    }

    public int getPlayerId() {
        return this.player.getId();
    }

    @Override
    public String getUsername() {
        return this.player.getData().getUsername();
    }

    @Override
    public String getMotto() {
        return this.player.getData().getMotto();
    }

    @Override
    public String getFigure() {
        return this.player.getData().getFigure();
    }

    @Override
    public String getGender() {
        return this.player.getData().getGender();
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(this.getVirtualId());
        msg.writeString(this.getUsername());
        msg.writeString(this.getMotto());
        msg.writeString(this.getFigure());
        msg.writeInt(this.getVirtualId());

        msg.writeInt(this.getPosition().getX());
        msg.writeInt(this.getPosition().getY());
        msg.writeDouble(this.getPosition().getZ());

        msg.writeInt(2); // 2 = user 4 = bot
        msg.writeInt(1); // 1 = user 2 = pet 3 = bot

        msg.writeString(this.getGender().toLowerCase());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString("");
        msg.writeString("");
        msg.writeInt(0);
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Deprecated
    public void dispose() {
        this.leaveRoom(true, false, false);
    }
}
