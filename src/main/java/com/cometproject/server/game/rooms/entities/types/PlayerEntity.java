package com.cometproject.server.game.rooms.entities.types;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.PlayerEntityAccess;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.game.wired.types.TriggerType;
import com.cometproject.server.logging.types.RoomChatLogEntry;
import com.cometproject.server.network.messages.outgoing.room.access.DoorbellRequestComposer;
import com.cometproject.server.network.messages.outgoing.room.alerts.DoorbellNoAnswerComposer;
import com.cometproject.server.network.messages.outgoing.room.alerts.RoomErrorMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.alerts.RoomFullMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.IdleStatusMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.ModelAndIdMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.PapersMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.FloodFilterMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.OwnerRightsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.RoomRatingMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.pets.RoomPetDao;
import com.cometproject.server.utilities.attributes.Attributable;
import javolution.util.FastMap;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;
import java.util.Set;

public class PlayerEntity extends GenericEntity implements PlayerEntityAccess, Attributable {
    private Player player;

    private Map<String, Object> attributes = new FastMap<>();

    public PlayerEntity(Player player, int identifier, Position3D startPosition, int startBodyRotation, int startHeadRotation, Room roomInstance) {
        super(identifier, startPosition, startBodyRotation, startHeadRotation, roomInstance);

        this.player = player;

        if (this.player.isTeleporting())
            this.setOverriden(true);
    }

    @Override
    public void joinRoom(Room room, String password) {
        if (this.getRoom() == null) {
            this.player.getSession().send(HotelViewMessageComposer.compose());
            return;
        }

        // Room full or slot available
        if (this.getRoom().getEntities().playerCount() >= this.getRoom().getData().getMaxUsers() && !this.player.getPermissions().hasPermission("room_enter_full")) {
            this.player.getSession().send(RoomFullMessageComposer.compose());
            this.player.getSession().send(HotelViewMessageComposer.compose());
            return;
        }

        // Room bans
        if (this.getRoom().getRights().hasBan(this.player.getId()) && !this.player.getPermissions().hasPermission("room_unkickable")) {
            // TODO: Proper ban message
            this.player.getSession().send(HotelViewMessageComposer.compose());
            return;
        }

        boolean isOwner = (this.getRoom().getData().getOwnerId() == this.player.getId());

        if (!isOwner && !this.player.getPermissions().hasPermission("room_enter_locked") && !this.isDoorbellAnswered() && !this.getPlayer().isTeleporting()) {
            if (this.getRoom().getData().getAccess().equals("password")) {
                boolean matched = false;

                if (RoomData.ENCRYPT_PASSWORDS) {
                    matched = BCrypt.checkpw(password, this.getRoom().getData().getPassword());
                } else {
                    matched = this.getRoom().getData().getPassword().equals(password);
                }

                if (!matched) {
                    this.player.getSession().send(RoomErrorMessageComposer.compose(-100002));
                    this.player.getSession().send(HotelViewMessageComposer.compose());
                    return;
                }
            } else if (this.getRoom().getData().getAccess().equals("doorbell")) {
                if (this.getRoom().getEntities().playerCount() < 1) {
                    this.player.getSession().send(DoorbellNoAnswerComposer.compose());
                    return;
                } else {
                    this.getRoom().getEntities().broadcastMessage(DoorbellRequestComposer.compose(this.getUsername()), true);
                    this.player.getSession().send(DoorbellRequestComposer.compose(""));
                    return;
                }
            }
        }

        this.getRoom().getEntities().addEntity(this);
        this.finalizeJoinRoom();
    }

    @Override
    protected void finalizeJoinRoom() {
        this.player.getSession().send(ModelAndIdMessageComposer.compose(this.getRoom().getModel().getId(), this.getVirtualId()));

        for (Map.Entry<String, String> decoration : this.getRoom().getData().getDecorations().entrySet()) {
            if (decoration.getKey().equals("wallpaper") || decoration.getKey().equals("floor")) {
                if (decoration.getValue().equals("0.0")) {
                    continue;
                }
            }

            this.player.getSession().send(PapersMessageComposer.compose(decoration.getKey(), decoration.getValue()));
        }

        int accessLevel = 0;

        if (this.getRoom().getData().getOwnerId() == this.player.getId() || this.player.getPermissions().hasPermission("room_full_control")) {
            this.addStatus("flatctrl 4", "useradmin");
            accessLevel = 4;
        } else if (this.getRoom().getRights().hasRights(this.player.getId())) {
            this.addStatus("flatctrl 1", "");
            accessLevel = 1;
        }

        this.player.getSession().send(AccessLevelMessageComposer.compose(accessLevel));

        if (this.getRoom().getData().getOwnerId() == this.player.getId() || this.player.getPermissions().hasPermission("room_full_control")) {
            this.player.getSession().send(OwnerRightsMessageComposer.compose());
        }

        this.player.getSession().send(RoomRatingMessageComposer.compose(this.getRoom().getData().getScore(), canRateRoom()));
    }

    public boolean canRateRoom() {
        if(!this.getRoom().hasAttribute("ratings") || !(this.getRoom().getAttribute("ratings") instanceof Set)) {
            return true;
        }

        Set<Integer> ratings = (Set<Integer>) this.getRoom().getAttribute("ratings");

        return !ratings.contains(this.getPlayerId());

    }

    @Override
    public void leaveRoom(boolean isOffline, boolean isKick, boolean toHotelView) {
        // Remove player's pets from room if they aren't owner
        if(this.getPlayer().getId() != this.getRoom().getData().getOwnerId() && this.getRoom().isActive) {
            for(PetEntity pet : this.getRoom().getEntities().getPetEntities()) {
                if(pet.getData().getOwnerId() == this.player.getId()) {
                    pet.leaveRoom(false);
                    RoomPetDao.updatePet(0, 0, 0, pet.getData().getId());

                    if(!isOffline) {
                        this.player.getPets().addPet(pet.getData());
                    }
                }
            }

            if(!isOffline) {
                this.player.getSession().send(PetInventoryMessageComposer.compose(this.player.getPets().getPets()));
            }
        }

        // Clear all  statuses
        this.getStatuses().clear();

        // Send leave room message to all current entities
        this.getRoom().getEntities().broadcastMessage(LeaveRoomMessageComposer.compose(this.getVirtualId()));

        // Sending this user to the hotel view?
        if (!isOffline && toHotelView) {
            this.getPlayer().getSession().send(HotelViewMessageComposer.compose());
            this.getPlayer().getSession().getPlayer().getMessenger().sendStatus(true, false);
        }

        if(isKick && !isOffline) {
            this.getPlayer().getSession().send(RoomErrorMessageComposer.compose(4008));
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

        if(!this.player.getPermissions().hasPermission("bypass_flood")) {
            if (time - this.player.getLastMessageTime() < 750) {
                this.player.setFloodFlag(this.player.getFloodFlag() + 1);

                if (this.player.getFloodFlag() >= 4) {
                    this.player.setFloodTime(30);
                    this.player.setFloodFlag(0);

                    this.player.getSession().send(FloodFilterMessageComposer.compose(player.getFloodTime()));
                }
            }

            if (player.getFloodTime() >= 1) {
                return false;
            }

            if(player.getLastMessage().equals(message) && message.length() > 15) {
                this.player.setFloodFlag(0);
                this.player.setFloodTime(30);

                this.player.getSession().send(FloodFilterMessageComposer.compose(player.getFloodTime()));
                return false;
            }

            player.setLastMessageTime(time);
            player.setLastMessage(message);
        }

        if (message.isEmpty() || message.length() > 100)
            return false;

        try {
            if (message.startsWith(":")) {
                if (CometManager.getCommands().isCommand(message.substring(1))) {
                    CometManager.getCommands().parse(message.substring(1), this.player.getSession());
                    return false;
                }
            }
        } catch (Exception e) {
            // command error?
        }


        if (this.getRoom().hasRoomMute() && !this.getPlayer().getPermissions().hasPermission("bypass_roommute") && this.getRoom().getData().getOwnerId() != this.player.getId()) {
            return false;
        }

        if (this.getRoom().getWired().trigger(TriggerType.ON_SAY, message, this)) {
            return false;
        }

        if (CometSettings.logChatToConsole) {
            this.getRoom().log.info(this.getPlayer().getData().getUsername() + ": " + message);
        }

        for (PetEntity entity : this.getRoom().getEntities().getPetEntities()) {
            if (message.split(" ").length > 0) {
                if (entity.getData().getName().toLowerCase().equals(message.split(" ")[0].toLowerCase())) {
                    if (entity.getAI().onTalk(this, message)) {
                        return false;
                    }
                }
            }
        }

        CometManager.getLoggingManager().queue(new RoomChatLogEntry(this.getRoom().getId(), this.getPlayerId(), message));

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
        msg.writeInt(this.getPlayer().getData().getAchievementPoints()); //achv points
        msg.writeBoolean(false);
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Deprecated
    public void dispose() {
        this.leaveRoom(true, false, false);
        this.attributes.clear();
    }

    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {
        if (this.attributes.containsKey(attributeKey)) {
            this.attributes.replace(attributeKey, attributeValue);
        } else {
            this.attributes.put(attributeKey, attributeValue);
        }
    }

    @Override
    public Object getAttribute(String attributeKey) {
        return this.attributes.get(attributeKey);
    }

    @Override
    public boolean hasAttribute(String attributeKey) {
        return this.attributes.containsKey(attributeKey);
    }

    @Override
    public void removeAttribute(String attributeKey) {
        this.attributes.remove(attributeKey);
    }
}
