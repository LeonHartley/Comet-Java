package com.cometsrv.game.rooms.entities.types;

import com.cometsrv.game.players.types.Player;
import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.entities.PlayerEntityAccess;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.outgoing.room.alerts.RoomFullMessageComposer;
import com.cometsrv.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometsrv.network.messages.outgoing.room.engine.ModelAndIdMessageComposer;
import com.cometsrv.network.messages.outgoing.room.engine.PapersMessageComposer;
import com.cometsrv.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometsrv.network.messages.outgoing.room.permissions.OwnerRightsMessageComposer;
import com.cometsrv.network.messages.types.Composer;

public class PlayerEntity extends GenericEntity implements PlayerEntityAccess {
    private Player player;

    public PlayerEntity(Player player, int identifier, Position3D startPosition, Room roomInstance) {
        super(identifier, startPosition, roomInstance);

        this.player = player;
    }

    @Override
    public void joinRoom(Room room, String password) {
        if (this.getRoom() == null) {
            this.player.getSession().send(HotelViewMessageComposer.compose());
            return;
        }

        // Is the room loaded
        if (!this.getRoom().isActive) {
            this.getRoom().load();
        }

        // Room full or slot available
        if (this.getRoom().getAvatars().count() >= this.getRoom().getData().getMaxUsers() && !this.player.getPermissions().hasPermission("room_enter_full")) {
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
    public void leaveRoom() {

    }

    @Override
    public void onChat(String message) {

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
}
