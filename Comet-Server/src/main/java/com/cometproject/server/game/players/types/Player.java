package com.cometproject.server.game.players.types;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.players.components.*;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.UpdateAvatarAspectMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.CurrenciesMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.SendCreditsMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.groups.GroupDao;
import com.cometproject.server.storage.queries.player.PlayerDao;
import javolution.util.FastMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {
    public static final int INFINITE_BALANCE = 999999;

    private int id;

    private PlayerSettings settings;
    private PlayerData data;
    private PlayerStatistics stats;

    private PlayerEntity avatar;
    private Session session;

    private PermissionComponent permissions;
    private InventoryComponent inventory;
    private SubscriptionComponent subscription;
    private MessengerComponent messenger;
    private RelationshipComponent relationships;
    private InventoryBotComponent bots;
    private PetComponent pets;
    private QuestComponent quests;

    private List<Integer> rooms = new ArrayList<>();
    private List<Integer> groups = new ArrayList<>();

    private List<Integer> ignoredPlayers = new ArrayList<>();

    private long roomLastMessageTime = 0;
    private double roomFloodTime = 0;
    private int roomFloodFlag = 0;

    private long messengerLastMessageTime = 0;
    private double messengerFloodTime = 0;
    private int messengerFloodFlag = 0;

    private int teleportId = 0;
    private String lastMessage = "";
    private int notifCooldown = 0;
    private int lastRoomId;

    private int lastGift = 0;
    private int lastRoomCreated;

    public boolean cancelPageOpen = false;

    private boolean isDeletingGroup = false;
    private long deletingGroupAttempt = 0;

    private boolean bypassRoomAuth;

    private int lastFigureUpdate = 0;

    public boolean isDisposed = false;

    public Player(ResultSet data, boolean isFallback) throws SQLException {
        this.id = data.getInt("playerId");

        if (isFallback) {
            this.settings = PlayerDao.getSettingsById(this.id);
            this.stats = PlayerDao.getStatisticsById(this.id);
        } else {
            this.settings = new PlayerSettings(data, true);
            this.stats = new PlayerStatistics(data, true);
        }

        this.data = new PlayerData(data);

        this.permissions = new PermissionComponent(this);
        this.inventory = new InventoryComponent(this);
        this.messenger = new MessengerComponent(this);
        this.subscription = new SubscriptionComponent(this);
        this.relationships = new RelationshipComponent(this);
        this.bots = new InventoryBotComponent(this);
        this.pets = new PetComponent(this);
        this.quests = new QuestComponent(this);

        this.groups = GroupDao.getIdsByPlayerId(this.id);

        this.avatar = null;
    }

    public void dispose() {
        if (this.getEntity() != null) {
            try {
                this.getEntity().leaveRoom(true, false, false);
            } catch (Exception e) {
                // Player failed to leave room
                this.getSession().getLogger().error("Error while disposing entity when player disconnects", e);
            }
        }

        this.getPets().dispose();
        this.getBots().dispose();
        this.getInventory().dispose();
        this.getMessenger().dispose();
        this.getRelationships().dispose();
        this.getQuests().dispose();

        this.session.getLogger().debug(this.getData().getUsername() + " logged out");

        PlayerDao.updatePlayerStatus(this, false, false);

        this.rooms.clear();
        this.rooms = null;

        this.groups.clear();
        this.groups = null;

        this.ignoredPlayers.clear();
        this.ignoredPlayers = null;

        this.settings = null;
        this.data = null;

        this.isDisposed = true;
    }

    public void sendBalance() {
        session.send(composeCurrenciesBalance());
        session.send(composeCreditBalance());
    }

    public MessageComposer composeCreditBalance() {
        return new SendCreditsMessageComposer(CometSettings.infiniteBalance ? Player.INFINITE_BALANCE : session.getPlayer().getData().getCredits());
    }

    public MessageComposer composeCurrenciesBalance() {
        Map<Integer, Integer> currencies = new FastMap<>();

        currencies.put(0, CometSettings.infiniteBalance ? Player.INFINITE_BALANCE : getData().getActivityPoints());
        currencies.put(105, getData().getVipPoints());
        currencies.put(5, getData().getVipPoints());

        return new CurrenciesMessageComposer(currencies);
    }

    public void loadRoom(int id, String password) {
        if (avatar != null && avatar.getRoom() != null) {
            avatar.leaveRoom(true, false, false);
            setEntity(null);
        }

        RoomInstance room = RoomManager.getInstance().get(id);

        if (room == null) {
            session.send(new HotelViewMessageComposer());
            return;
        }

        if (room.getEntities() == null) {
            return;
        }

        if (room.getEntities().getEntityByPlayerId(this.id) != null) {
            room.getEntities().getEntityByPlayerId(this.id).leaveRoom(true, false, false);
        }

        PlayerEntity playerEntity = room.getEntities().createEntity(this);
        setEntity(playerEntity);

        playerEntity.joinRoom(room, password);
    }

    public void poof() {
        this.getSession().send(new UpdateInfoMessageComposer(-1, this.getData().getFigure(), this.getData().getGender(), this.getData().getMotto(), this.getData().getAchievementPoints()));
        this.getSession().send(new UpdateAvatarAspectMessageComposer(this.getData().getFigure(), this.getData().getGender()));

        if (this.getEntity() != null && this.getEntity().getRoom() != null && this.getEntity().getRoom().getEntities() != null) {
            this.getEntity().unIdle();
            this.getEntity().getRoom().getEntities().broadcastMessage(new UpdateInfoMessageComposer(this.getEntity()));
        }
    }

    public void ignorePlayer(int playerId) {
        if(this.ignoredPlayers == null) {
            this.ignoredPlayers = new ArrayList<>();
        }

        this.ignoredPlayers.add(playerId);
    }

    public void unignorePlayer(int playerId) {
        this.ignoredPlayers.remove((Integer) playerId);
    }

    public boolean ignores(int playerId) {
        return this.ignoredPlayers != null && this.ignoredPlayers.contains(playerId);
    }

    public List<Integer> getRooms() {
        return rooms;
    }

    public void setRooms(List<Integer> rooms) {
        this.rooms = rooms;
    }

    public void setSession(Session client) {
        this.session = client;
    }

    public void setEntity(PlayerEntity avatar) {
        this.avatar = avatar;
    }

    public PlayerEntity getEntity() {
        return this.avatar;
    }

    public Session getSession() {
        return this.session;
    }

    public PlayerData getData() {
        return this.data;
    }

    public PlayerStatistics getStats() {
        return this.stats;
    }

    public PermissionComponent getPermissions() {
        return this.permissions;
    }

    public MessengerComponent getMessenger() {
        return this.messenger;
    }

    public InventoryComponent getInventory() {
        return this.inventory;
    }

    public SubscriptionComponent getSubscription() {
        return this.subscription;
    }

    public RelationshipComponent getRelationships() {
        return this.relationships;
    }

    public InventoryBotComponent getBots() {
        return this.bots;
    }

    public PetComponent getPets() {
        return this.pets;
    }

    public QuestComponent getQuests() {
        return quests;
    }

    public PlayerSettings getSettings() {
        return this.settings;
    }

    public int getId() {
        return this.id;
    }

    public boolean isTeleporting() {
        return this.teleportId != 0;
    }

    public int getTeleportId() {
        return this.teleportId;
    }

    public void setTeleportId(int teleportId) {
        this.teleportId = teleportId;
    }

    public long getRoomLastMessageTime() {
        return roomLastMessageTime;
    }

    public void setRoomLastMessageTime(long roomLastMessageTime) {
        this.roomLastMessageTime = roomLastMessageTime;
    }

    public double getRoomFloodTime() {
        return roomFloodTime;
    }

    public void setRoomFloodTime(double roomFloodTime) {
        this.roomFloodTime = roomFloodTime;
    }

    public int getRoomFloodFlag() {
        return roomFloodFlag;
    }

    public void setRoomFloodFlag(int roomFloodFlag) {
        this.roomFloodFlag = roomFloodFlag;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<Integer> getGroups() {
        return groups;
    }

    public int getNotifCooldown() {
        return this.notifCooldown;
    }

    public void setNotifCooldown(int notifCooldown) {
        this.notifCooldown = notifCooldown;
    }

    public int getLastRoomId() {
        return lastRoomId;
    }

    public void setLastRoomId(int lastRoomId) {
        this.lastRoomId = lastRoomId;
    }

    public int getLastGift() {
        return lastGift;
    }

    public void setLastGift(int lastGift) {
        this.lastGift = lastGift;
    }

    public long getMessengerLastMessageTime() {
        return messengerLastMessageTime;
    }

    public void setMessengerLastMessageTime(long messengerLastMessageTime) {
        this.messengerLastMessageTime = messengerLastMessageTime;
    }

    public double getMessengerFloodTime() {
        return messengerFloodTime;
    }

    public void setMessengerFloodTime(double messengerFloodTime) {
        this.messengerFloodTime = messengerFloodTime;
    }

    public int getMessengerFloodFlag() {
        return messengerFloodFlag;
    }

    public void setMessengerFloodFlag(int messengerFloodFlag) {
        this.messengerFloodFlag = messengerFloodFlag;
    }

    public boolean isDeletingGroup() {
        return isDeletingGroup;
    }

    public void setDeletingGroup(boolean isDeletingGroup) {
        this.isDeletingGroup = isDeletingGroup;
    }

    public long getDeletingGroupAttempt() {
        return deletingGroupAttempt;
    }

    public void setDeletingGroupAttempt(long deletingGroupAttempt) {
        this.deletingGroupAttempt = deletingGroupAttempt;
    }

    public void bypassRoomAuth(final boolean bypassRoomAuth) {
        this.bypassRoomAuth = bypassRoomAuth;
    }

    public boolean isBypassingRoomAuth() {
        return bypassRoomAuth;
    }

    public int getLastFigureUpdate() {
        return lastFigureUpdate;
    }

    public void setLastFigureUpdate(int lastFigureUpdate) {
        this.lastFigureUpdate = lastFigureUpdate;
    }
}
