package com.cometproject.server.game.rooms.objects.entities.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.commands.CommandManager;
import com.cometproject.server.game.commands.vip.TransformCommand;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.players.data.PlayerData;
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
import com.cometproject.server.logging.entries.RoomVisitLogEntry;
import com.cometproject.server.network.messages.incoming.room.engine.InitializeRoomMessageEvent;
import com.cometproject.server.network.messages.outgoing.room.access.DoorbellRequestComposer;
import com.cometproject.server.network.messages.outgoing.room.alerts.DoorbellNoAnswerComposer;
import com.cometproject.server.network.messages.outgoing.room.alerts.RoomConnectionErrorMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.alerts.RoomErrorMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.IdleStatusMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.MutedMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.PapersMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.events.RoomPromotionMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.FloodFilterMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.YouAreControllerMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.RoomRatingMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.utilities.attributes.Attributable;
import javolution.util.FastMap;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;


public class PlayerEntity extends GenericEntity implements PlayerEntityAccess, Attributable {
    private Player player;
    private PlayerData playerData;

    private Map<String, Object> attributes = new FastMap<>();
    private RoomVisitLogEntry visitLogEntry;

    private boolean isFinalized = false;
    private boolean isKicked = false;

    private GameTeam gameTeam = GameTeam.NONE;
    private int kickWalkStage = 0;

    public PlayerEntity(Player player, int identifier, Position startPosition, int startBodyRotation, int startHeadRotation, Room roomInstance) {
        super(identifier, startPosition, startBodyRotation, startHeadRotation, roomInstance);

        this.player = player;

        // create reference to the PlayerDa
        this.playerData = player.getData();

        if (this.getPlayer().isTeleporting())
            this.setOverriden(true);

        if (LogManager.ENABLED)
            this.visitLogEntry = LogManager.getInstance().getStore().getRoomVisitContainer().put(player.getId(), roomInstance.getId(), Comet.getTime());
    }

    @Override
    public void joinRoom(Room room, String password) {
        if (this.getRoom() == null) {
            this.getPlayer().getSession().send(new HotelViewMessageComposer());
            return;
        }

        // Room full, no slot available
        if (this.getPlayerId() != this.getRoom().getData().getOwnerId() && this.getRoom().getEntities().playerCount() >= this.getRoom().getData().getMaxUsers() &&
                !this.getPlayer().getPermissions().hasPermission("room_enter_full")) {
            this.getPlayer().getSession().send(new RoomConnectionErrorMessageComposer(1, ""));
            this.getPlayer().getSession().send(new HotelViewMessageComposer());
            return;
        }

        // Room bans
        if (this.getRoom().getRights().hasBan(this.getPlayerId()) && !this.getPlayer().getPermissions().hasPermission("room_unkickable")) {
            this.getPlayer().getSession().send(new RoomConnectionErrorMessageComposer(4, ""));
            return;
        }

        boolean isOwner = (this.getRoom().getData().getOwnerId() == this.getPlayerId());

        if (!this.getPlayer().isBypassingRoomAuth() && (!isOwner && !this.getPlayer().getPermissions().hasPermission("room_enter_locked") && !this.isDoorbellAnswered()) && !this.getPlayer().isTeleporting()) {
            if (this.getRoom().getData().getAccess().equals("password")) {
                boolean matched;

                if (CometSettings.roomPasswordEncryptionEnabled) {
                    matched = BCrypt.checkpw(password, this.getRoom().getData().getPassword());
                } else {
                    matched = this.getRoom().getData().getPassword().equals(password);
                }

                if (!matched) {
                    this.getPlayer().getSession().send(new RoomErrorMessageComposer(-100002));
                    this.getPlayer().getSession().send(new HotelViewMessageComposer());
                    return;
                }
            } else if (this.getRoom().getData().getAccess().equals("doorbell")) {
                if (this.getRoom().getEntities().playerCount() < 1) {
                    this.getPlayer().getSession().send(new DoorbellNoAnswerComposer());
                    return;
                } else {
                    this.getRoom().getEntities().broadcastMessage(new DoorbellRequestComposer(this.getUsername()), true);
                    this.getPlayer().getSession().send(new DoorbellRequestComposer(""));
                    return;
                }
            }
        }

        this.getPlayer().bypassRoomAuth(false);
        this.getPlayer().setTeleportId(0);

        this.getRoom().getEntities().increasePlayerCount();
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

            this.getPlayer().getSession().send(new PapersMessageComposer(decoration.getKey(), decoration.getValue()));
        }

        int accessLevel = 0;

        if (this.getRoom().getData().getOwnerId() == this.getPlayerId() || this.getPlayer().getPermissions().hasPermission("room_full_control")) {
            this.addStatus(RoomEntityStatus.CONTROLLER, "4");
            accessLevel = 4;
        } else if (this.getRoom().getRights().hasRights(this.getPlayerId())) {
            this.addStatus(RoomEntityStatus.CONTROLLER, "1");
            accessLevel = 1;
        }

        this.getPlayer().getSession().send(new AccessLevelMessageComposer(accessLevel));

        if (this.getRoom().getData().getOwnerId() == this.getPlayerId() || this.getPlayer().getPermissions().hasPermission("room_full_control")) {
            this.getPlayer().getSession().send(new YouAreControllerMessageComposer());
        }

        this.getPlayer().getSession().send(new RoomRatingMessageComposer(this.getRoom().getData().getScore(), this.canRateRoom()));

        InitializeRoomMessageEvent.heightmapMessageEvent.handle(this.getPlayer().getSession(), null);
        InitializeRoomMessageEvent.addUserToRoomMessageEvent.handle(this.getPlayer().getSession(), null);

        if (RoomManager.getInstance().hasPromotion(this.getRoom().getId())) {
            this.getPlayer().getSession().send(new RoomPromotionMessageComposer(this.getRoom().getData(), this.getRoom().getPromotion()));
        } else {
            this.getPlayer().getSession().send(new RoomPromotionMessageComposer(null, null));
        }

        this.isFinalized = true;
    }

    public boolean canRateRoom() {
        return !this.getRoom().getRatings().contains(this.getPlayerId());
    }

    @Override
    public void leaveRoom(boolean isOffline, boolean isKick, boolean toHotelView) {
        for (RoomItemFloor floorItem : this.getRoom().getItems().getFloorItems()) {
            if (floorItem == null) continue;
            floorItem.onEntityLeaveRoom(this);
        }

        // Check and cancel any active trades
        Trade trade = this.getRoom().getTrade().get(this);

        if (trade != null) {
            trade.cancel(this.getPlayerId());
        }

        if (this.getMountedEntity() != null) {
            this.getMountedEntity().setOverriden(false);
            this.getMountedEntity().setHasMount(false);
        }

        // Step off
        for (RoomItemFloor item : this.getRoom().getItems().getItemsOnSquare(this.getPosition().getX(), this.getPosition().getY())) {
            if (item == null) continue;
            item.onEntityStepOff(this);
        }

        if (isKick && !isOffline && this.getPlayer() != null && this.getPlayer().getSession() != null) {
            this.getPlayer().getSession().send(new RoomErrorMessageComposer(4008));
        }

        // Send leave room message to all current entities
        this.getRoom().getEntities().broadcastMessage(new LeaveRoomMessageComposer(this.getId()));

        // Sending this user to the hotel view?
        if (!isOffline && toHotelView && this.getPlayer() != null && this.getPlayer().getSession() != null) {
            this.getPlayer().getSession().send(new HotelViewMessageComposer());
            this.getPlayer().getSession().getPlayer().getMessenger().sendStatus(true, false);
        }

        // Remove entity from the room
        this.getRoom().getEntities().removeEntity(this);

        if (this.player != null) {
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
    }

    @Override
    public boolean onChat(String message) {
        long time = System.currentTimeMillis();

        if (!this.getPlayer().getPermissions().hasPermission("bypass_flood")) {
            if (time - this.getPlayer().getRoomLastMessageTime() < 750) {
                this.getPlayer().setRoomFloodFlag(this.getPlayer().getRoomFloodFlag() + 1);

                if (this.getPlayer().getRoomFloodFlag() >= 5) {
                    this.getPlayer().setRoomFloodTime(30);
                    this.getPlayer().setRoomFloodFlag(0);

                    this.getPlayer().getSession().send(new FloodFilterMessageComposer(player.getRoomFloodTime()));
                }
            } else {
                this.getPlayer().setRoomFloodFlag(0);
            }

            if (player.getRoomFloodTime() >= 1) {
                return false;
            }

            player.setRoomLastMessageTime(time);
            player.setLastMessage(message);
        }

        if (message.isEmpty() || message.length() > 100)
            return false;

        try {
            if (message.startsWith(":")) {
                String cmd = message.substring(1);

                if (CommandManager.getInstance().isCommand(cmd)) {
                    if (CommandManager.getInstance().parse(cmd, this.getPlayer().getSession()))
                        return false;
                } else if (CommandManager.getInstance().getNotifications().isNotificationExecutor(cmd, this.getPlayer().getData().getRank())) {
                    CommandManager.getInstance().getNotifications().execute(this.player, cmd);
                }
            }
        } catch (Exception e) {
            CometManager.getLogger().error("Error while executing command", e);
            return false;
        }

        if (this.isRoomMuted() && !this.getPlayer().getPermissions().hasPermission("bypass_roommute") && this.getRoom().getData().getOwnerId() != this.getPlayerId()) {
            return false;
        }

        if ((this.getRoom().getRights().hasMute(this.getPlayerId()) || BanManager.getInstance().isMuted(this.getPlayerId())) && !this.getPlayer().getPermissions().hasPermission("bypass_roommute")) {
            this.getPlayer().getSession().send(new MutedMessageComposer(this.getRoom().getRights().getMuteTime(this.getPlayerId())));

            return false;
        }

        if (WiredTriggerPlayerSaysKeyword.executeTriggers(this, message)) {
            return false;
        }

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
            if (entity.getAI().onTalk(this, message)) break;
        }
    }

    @Override
    public boolean onRoomDispose() {
        // Clear all  statuses
        this.getStatuses().clear();

        // Send leave room message to all current entities
        this.getRoom().getEntities().broadcastMessage(new LeaveRoomMessageComposer(this.getId()));

        // Sending this user to the hotel view?
        this.getPlayer().getSession().send(new HotelViewMessageComposer());
        this.getPlayer().getSession().getPlayer().getMessenger().sendStatus(true, false);

        // Check and cancel any active trades
        Trade trade = this.getRoom().getTrade().get(this);

        if (trade != null) {
            trade.cancel(this.getPlayerId());
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

        this.getRoom().getEntities().broadcastMessage(new IdleStatusMessageComposer(this.getPlayerId(), true));
    }

    public int getPlayerId() {
        return this.playerData.getId();
    }

    @Override
    public String getUsername() {
        return this.playerData.getUsername();
    }

    @Override
    public String getMotto() {
        return this.playerData.getMotto();
    }

    @Override
    public String getFigure() {
        return this.playerData.getFigure();
    }

    @Override
    public String getGender() {
        return this.playerData.getGender();
    }

    @Override
    public void compose(Composer msg) {
        if (this.hasAttribute("transformation")) {
            String[] transformationData = ((String) this.getAttribute("transformation")).split("#");

            TransformCommand.composeTransformation(msg, transformationData, this);
            return;
        }

        msg.writeInt(this.getPlayerId());
        msg.writeString(this.getUsername().replace("<", "").replace(">", "")); // Client sometimes parses the username as HTML...
        msg.writeString(this.getMotto());
        msg.writeString(this.getFigure());
        msg.writeInt(this.getId());

        msg.writeInt(this.getPosition().getX());
        msg.writeInt(this.getPosition().getY());
        msg.writeDouble(this.getPosition().getZ());

        msg.writeInt(this.getBodyRotation()); // 2 = user 4 = bot
        msg.writeInt(1); // 1 = user 2 = pet 3 = bot

        msg.writeString(this.getGender().toLowerCase());

        if (this.getPlayer().getData().getFavouriteGroup() == 0) {
            msg.writeInt(-1);
            msg.writeInt(-1);
            msg.writeInt(0);
        } else {
            Group group = GroupManager.getInstance().get(this.getPlayer().getData().getFavouriteGroup());

            if (group == null) {
                msg.writeInt(-1);
                msg.writeInt(-1);
                msg.writeInt(0);

                this.getPlayer().getData().setFavouriteGroup(0);
                this.getPlayer().getData().save();
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
