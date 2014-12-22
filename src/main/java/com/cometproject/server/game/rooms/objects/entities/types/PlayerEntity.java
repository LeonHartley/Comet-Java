package com.cometproject.server.game.rooms.objects.entities.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.commands.CommandManager;
import com.cometproject.server.game.commands.vip.TransformCommand;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.PlayerEntityAccess;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerPlayerSaysKeyword;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameTeam;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.entries.RoomChatLogEntry;
import com.cometproject.server.logging.entries.RoomVisitLogEntry;
import com.cometproject.server.network.messages.incoming.room.engine.InitializeRoomMessageEvent;
import com.cometproject.server.network.messages.outgoing.room.access.DoorbellRequestComposer;
import com.cometproject.server.network.messages.outgoing.room.alerts.DoorbellNoAnswerComposer;
import com.cometproject.server.network.messages.outgoing.room.alerts.RoomConnectionErrorMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.alerts.RoomErrorMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.IdleStatusMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.PapersMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.events.RoomPromotionMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.FloodFilterMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.OwnerRightsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.RoomRatingMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.utilities.attributes.Attributable;
import javolution.util.FastMap;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;
import java.util.Set;


public class PlayerEntity extends GenericEntity implements PlayerEntityAccess, Attributable {
    private Player player;
    private int playerId;

    private Map<String, Object> attributes = new FastMap<>();
    private RoomVisitLogEntry visitLogEntry;

    private boolean isFinalized = false;
    private boolean isKicked = false;

    private GameTeam gameTeam = GameTeam.NONE;
    private int kickWalkStage = 0;

    public PlayerEntity(Player player, int identifier, Position startPosition, int startBodyRotation, int startHeadRotation, Room roomInstance) {
        super(identifier, startPosition, startBodyRotation, startHeadRotation, roomInstance);

        this.player = player;
        this.playerId = player.getId();

        if (this.player.isTeleporting())
            this.setOverriden(true);

        if (LogManager.ENABLED)
            this.visitLogEntry = LogManager.getInstance().getStore().getRoomVisitContainer().put(player.getId(), roomInstance.getId(), Comet.getTime());
    }

    @Override
    public void joinRoom(Room room, String password) {
        if (this.getRoom() == null) {
            this.player.getSession().send(HotelViewMessageComposer.compose());
            return;
        }

        // Room full, no slot available
        if (this.getPlayer().getId() != this.getRoom().getData().getOwnerId() && this.getRoom().getEntities().playerCount() >= this.getRoom().getData().getMaxUsers() &&
                !this.player.getPermissions().hasPermission("room_enter_full")) {
            this.player.getSession().send(RoomConnectionErrorMessageComposer.compose(1, ""));
            this.player.getSession().send(HotelViewMessageComposer.compose());
            return;
        }

        // Room bans
        if (this.getRoom().getRights().hasBan(this.player.getId()) && !this.player.getPermissions().hasPermission("room_unkickable")) {
            this.player.getSession().send(RoomConnectionErrorMessageComposer.compose(4, ""));
            return;
        }

        boolean isOwner = (this.getRoom().getData().getOwnerId() == this.player.getId());

        if ((!isOwner && !this.player.getPermissions().hasPermission("room_enter_locked") && !this.isDoorbellAnswered()) && !this.getPlayer().isTeleporting()) {
            if (this.getRoom().getData().getAccess().equals("password")) {
                boolean matched;

                if (CometSettings.roomPasswordEncryptionEnabled) {
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

        player.setTeleportId(0);

        this.getRoom().getEntities().addEntity(this);
        this.finalizeJoinRoom();
    }

    @Override
    protected void finalizeJoinRoom() {
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
            this.addStatus(RoomEntityStatus.CONTROLLER, "4");
            accessLevel = 4;
        } else if (this.getRoom().getRights().hasRights(this.player.getId())) {
            this.addStatus(RoomEntityStatus.CONTROLLER, "1");
            accessLevel = 1;
        }

        this.player.getSession().send(AccessLevelMessageComposer.compose(accessLevel));

        if (this.getRoom().getData().getOwnerId() == this.player.getId() || this.player.getPermissions().hasPermission("room_full_control")) {
            this.player.getSession().send(OwnerRightsMessageComposer.compose());
        }

        this.player.getSession().send(RoomRatingMessageComposer.compose(this.getRoom().getData().getScore(), canRateRoom()));

        InitializeRoomMessageEvent.heightmapMessageEvent.handle(this.player.getSession(), null);
        InitializeRoomMessageEvent.addUserToRoomMessageEvent.handle(this.player.getSession(), null);

        if (RoomManager.getInstance().hasPromotion(this.getRoom().getId())) {
            this.player.getSession().send(RoomPromotionMessageComposer.compose(this.getRoom().getData(), this.getRoom().getPromotion()));
        } else {
            this.player.getSession().send(RoomPromotionMessageComposer.compose(null, null));
        }

        this.isFinalized = true;
    }

    public boolean canRateRoom() {
        if (!this.getRoom().hasAttribute("ratings") || !(this.getRoom().getAttribute("ratings") instanceof Set)) {
            return true;
        }

        Set<Integer> ratings = (Set<Integer>) this.getRoom().getAttribute("ratings");

        return !ratings.contains(this.getPlayerId());

    }

    @Override
    public void leaveRoom(boolean isOffline, boolean isKick, boolean toHotelView) {
        for (RoomItemFloor floorItem : this.getRoom().getItems().getFloorItems()) {
            if (floorItem == null) continue;
            floorItem.onEntityLeaveRoom(this);
        }

        if (this.getMountedEntity() != null) {
            this.getMountedEntity().setOverriden(false);
            this.getMountedEntity().setHasMount(false);
        }

        // Step off
        for (RoomItemFloor item : this.getRoom().getItems().getItemsOnSquare(this.getPosition().getX(), this.getPosition().getY())) {
            if(item == null) continue;
            item.onEntityStepOff(this);
        }

        if (isKick && !isOffline) {
            this.getPlayer().getSession().send(RoomErrorMessageComposer.compose(4008));
        }

        // Send leave room message to all current entities
        this.getRoom().getEntities().broadcastMessage(LeaveRoomMessageComposer.compose(this.getId()));

        // Sending this user to the hotel view?
        if (!isOffline && toHotelView) {
            this.getPlayer().getSession().send(HotelViewMessageComposer.compose());
            this.getPlayer().getSession().getPlayer().getMessenger().sendStatus(true, false);
        }

        // Check and cancel any active trades
        Trade trade = this.getRoom().getTrade().get(this.getPlayer().getEntity());

        if (trade != null) {
            trade.cancel(this.getPlayer().getId());
        }

        // Remove entity from the room
        this.getRoom().getEntities().removeEntity(this);

        if(this.player != null) {
            this.getPlayer().setEntity(null);
        }

        if (this.visitLogEntry != null) {
            this.visitLogEntry.setExitTime((int) Comet.getTime());

            LogManager.getInstance().getStore().getRoomVisitContainer().updateExit(this.visitLogEntry);
        }

        this.getStatuses().clear();
        this.attributes.clear();

        // De-reference things
        this.player = null;
    }

    @Override
    public void finalizeLeaveRoom() {
        // not used, could be removed?
    }

    @Override
    public void kick() {
        this.isKicked = true;
        this.setCanWalk(false);

        this.moveTo(this.getRoom().getModel().getDoorX(), this.getRoom().getModel().getDoorY());

        if(this.getProcessingPath() == null) {
            this.leaveRoom(false, true, true);
        }
    }

    @Override
    public boolean onChat(String message) {
        long time = System.currentTimeMillis();

        if (!this.player.getPermissions().hasPermission("bypass_flood")) {
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

            if (player.getLastMessage().equals(message) && message.length() > 15) {
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
                String cmd = message.substring(1);

                if (CommandManager.getInstance().isCommand(cmd)) {
                    if (CommandManager.getInstance().parse(cmd, this.player.getSession()))
                        return false;
                } else if (CommandManager.getInstance().getNotifications().isNotificationExecutor(cmd, this.player.getData().getRank())) {
                    CommandManager.getInstance().getNotifications().execute(this.player, cmd);
                }
            }
        } catch (Exception e) {
            CometManager.getLogger().error("Error while executing command", e);
            return false;
        }


        if (this.getRoom().hasRoomMute() && !this.getPlayer().getPermissions().hasPermission("bypass_roommute") && this.getRoom().getData().getOwnerId() != this.player.getId()) {
            return false;
        }

        if (BanManager.getInstance().isMuted(this.getPlayerId()) && !this.getPlayer().getPermissions().hasPermission("bypass_roommute")) {
            return false;
        }

        if (WiredTriggerPlayerSaysKeyword.executeTriggers(this, message)) {
            return false;
        }

        if (LogManager.ENABLED)
            LogManager.getInstance().getStore().getLogEntryContainer().put(new RoomChatLogEntry(this.getRoom().getId(), this.getPlayerId(), message));

//        for (PetEntity entity : this.getRoom().getEntities().getPetEntities()) {
//            if (message.split(" ").length > 0) {
//                if (entity.getData().getName().toLowerCase().equals(message.split(" ")[0].toLowerCase())) {
//                    if (entity.getAI().onTalk(this, message)) {
//                        return false;
//                    }
//                }
//            }
//        }

        this.unIdle();
        return true;
    }

    public void postChat(String message) {
        for (BotEntity entity : this.getRoom().getEntities().getBotEntities()) {
            if(entity.getAI().onTalk(this, message)) break;
        }
    }

    @Override
    public boolean onRoomDispose() {
        // Clear all  statuses
        this.getStatuses().clear();

        // Send leave room message to all current entities
        this.getRoom().getEntities().broadcastMessage(LeaveRoomMessageComposer.compose(this.getId()));

        // Sending this user to the hotel view?
        this.getPlayer().getSession().send(HotelViewMessageComposer.compose());
        this.getPlayer().getSession().getPlayer().getMessenger().sendStatus(true, false);

        // Check and cancel any active trades
        Trade trade = this.getRoom().getTrade().get(this.getPlayer().getEntity());

        if (trade != null) {
            trade.cancel(this.getPlayer().getId());
        }

        if (this.visitLogEntry != null) {
            this.visitLogEntry.setExitTime((int) Comet.getTime());

            if (LogManager.ENABLED)
                LogManager.getInstance().getStore().getRoomVisitContainer().updateExit(this.visitLogEntry);
        }

        // De-reference things
        this.getPlayer().setEntity(null);
        this.player = null;
        return false;
    }

    @Override
    public void setIdle() {
        super.setIdle();

        this.getRoom().getEntities().broadcastMessage(IdleStatusMessageComposer.compose(this.player.getId(), true));
    }

    public int getPlayerId() {
        return this.playerId;
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
        if (this.hasAttribute("transformation")) {
            String[] transformationData = ((String) this.getAttribute("transformation")).split("#");

            TransformCommand.composeTransformation(msg, transformationData, this);
            return;
        }

        msg.writeInt(this.getPlayerId());
        msg.writeString(this.getUsername());
        msg.writeString(this.getMotto());
        msg.writeString(this.getFigure());
        msg.writeInt(this.getId());

        msg.writeInt(this.getPosition().getX());
        msg.writeInt(this.getPosition().getY());
        msg.writeDouble(this.getPosition().getZ());

        msg.writeInt(this.getBodyRotation()); // 2 = user 4 = bot
        msg.writeInt(1); // 1 = user 2 = pet 3 = bot

        msg.writeString(this.getGender().toLowerCase());

        if (this.player.getData().getFavouriteGroup() == 0) {
            msg.writeInt(-1);
            msg.writeInt(-1);
            msg.writeInt(0);
        } else {
            Group group = GroupManager.getInstance().get(this.player.getData().getFavouriteGroup());

            if (group == null) {
                msg.writeInt(-1);
                msg.writeInt(-1);
                msg.writeInt(0);

                this.player.getData().setFavouriteGroup(0);
                this.player.getData().save();
            } else {
                msg.writeInt(group.getId());
                msg.writeInt(2);
                msg.writeString(group.getData().getTitle());
                msg.writeString("");
            }
        }

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

    public boolean isFinalized() {
        return isFinalized;
    }

    public GameTeam getGameTeam() {
        return gameTeam;
    }

    public void setGameTeam(GameTeam gameTeam) {
        this.gameTeam = gameTeam;
    }

    public boolean isKicked() {
        return isKicked;
    }

    public int getKickWalkStage() {
        return kickWalkStage;
    }

    public void increaseKickWalkStage() {
        this.kickWalkStage++;
    }
}
