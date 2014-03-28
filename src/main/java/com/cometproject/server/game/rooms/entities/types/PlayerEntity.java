package com.cometproject.server.game.rooms.entities.types;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.Square;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.PlayerEntityAccess;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.game.wired.types.TriggerType;
import com.cometproject.server.network.messages.outgoing.room.alerts.RoomFullMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.IdleStatusMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.ModelAndIdMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.PapersMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.FloodFilterMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.OwnerRightsMessageComposer;
import com.cometproject.server.network.messages.types.Composer;

import java.util.LinkedList;
import java.util.Map;

public class PlayerEntity extends GenericEntity implements PlayerEntityAccess {
    private Player player;

    public PlayerEntity(Player player, int identifier, Position3D startPosition, int startBodyRotation, int startHeadRotation, Room roomInstance) {
        super(identifier, startPosition, startBodyRotation, startHeadRotation, roomInstance);

        this.player = player;

        if(this.player.isTeleporting())
            this.setIsInTeleporter(true);
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

                return;
            }
        }

        this.finalizeJoinRoom();
    }

    @Override
    protected void finalizeJoinRoom() {
        this.player.getSession().send(ModelAndIdMessageComposer.compose(this.getRoom().getModel().getId(), this.getVirtualId()));

        for(Map.Entry<String, String> decoration : this.getRoom().getData().getDecorations().entrySet()) {
            if(decoration.getKey().equals("wallpaper") || decoration.getKey().equals("floor")) {
                if(decoration.getValue().equals("0.0")) {
                    continue;
                }
            }

            this.player.getSession().send(PapersMessageComposer.compose(decoration.getKey(), decoration.getValue()));
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

        // Also could be useful for bot trading etc

        // Check and cancel any active trades
        Trade trade = this.getRoom().getTrade().get(this.getPlayer().getEntity());

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

        if (time - this.player.lastMessage < 500) { // TODO: add flood bypass for staff with permission or something
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

        if(message.isEmpty())
            return false;

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

        for(BotEntity entity : this.getRoom().getEntities().getBotEntities()) {
            if(entity.getUsername().replace(" ", "_").toLowerCase().equals(message.split(" ")[0].toLowerCase())) {
                if(entity.getAI().onTalk(this, message.replace(message.split(" ")[0] + " ", ""))) {
                    return false;
                }
            }
        }

        this.getRoom().getChatlog().add(message, this.getPlayer().getId());

        this.unIdle();
        return true;
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
        msg.writeInt(this.getPlayerId());
        //msg.writeInt(this.getVirtualId());
        msg.writeString(this.getUsername());
        msg.writeString(this.getMotto());
        msg.writeString(this.getFigure());
        msg.writeInt(this.getVirtualId());

        msg.writeInt(this.getPosition().getX());
        msg.writeInt(this.getPosition().getY());
        msg.writeDouble(this.getPosition().getZ());

        msg.writeInt(this.getBodyRotation()); // 2 = user 4 = bot
        msg.writeInt(1); // 1 = user 2 = pet 3 = bot

        msg.writeString(this.getGender().toLowerCase());
        msg.writeInt(-1);
        msg.writeInt(-1);
        msg.writeInt(0);
        msg.writeInt(0); //achv points
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
