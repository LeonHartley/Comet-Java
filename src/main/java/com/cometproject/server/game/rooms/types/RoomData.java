package com.cometproject.server.game.rooms.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.navigator.types.Category;
import com.cometproject.server.game.rooms.types.misc.settings.RoomBanState;
import com.cometproject.server.game.rooms.types.misc.settings.RoomKickState;
import com.cometproject.server.game.rooms.types.misc.settings.RoomMuteState;
import com.cometproject.server.game.rooms.types.misc.settings.RoomTradeState;
import com.cometproject.server.storage.queries.rooms.RoomDao;
import javolution.util.FastMap;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class RoomData {
    private int id;
    private String name;
    private String description;
    private int ownerId;
    private String owner;
    private int category;
    private int maxUsers;
    private String access;
    private String password;
    private String originalPassword;
    private RoomTradeState tradeState;

    private int score;

    private String[] tags;
    private Map<String, String> decorations;

    private String model;

    private boolean hideWalls;
    private int thicknessWall;
    private int thicknessFloor;
    private boolean allowWalkthrough;
    private boolean allowPets;
    private String heightmap;

    private RoomMuteState muteState;
    private RoomKickState kickState;
    private RoomBanState banState;

    private int bubbleMode;
    private int bubbleType;
    private int bubbleScroll;
    private int chatDistance;

    private int antiFloodSettings;

    private long lastReferenced = Comet.getTime();

    public RoomData(ResultSet room) throws SQLException {
        this.id = room.getInt("id");
        this.name = room.getString("name");
        this.description = room.getString("description");
        this.ownerId = room.getInt("owner_id");
        this.owner = room.getString("owner");
        this.category = room.getInt("category");
        this.maxUsers = room.getInt("max_users");
        this.access = room.getString("access_type");
        this.password = room.getString("password");
        this.originalPassword = this.password;

        this.score = room.getInt("score");

        this.tags = room.getString("tags").split(",");
        this.decorations = new FastMap<>();

        String[] decorations = room.getString("decorations").split(",");

        for (int i = 0; i < decorations.length; i++) {
            String[] decoration = decorations[i].split("=");

            if (decoration.length == 2)
                this.decorations.put(decoration[0], decoration[1]);
        }

        this.model = room.getString("model");

        this.hideWalls = room.getString("hide_walls").equals("1");
        this.thicknessWall = room.getInt("thickness_wall");
        this.thicknessFloor = room.getInt("thickness_floor");
        this.allowWalkthrough = room.getString("allow_walkthrough").equals("1");
        this.allowPets = room.getString("allow_pets").equals("1");
        this.heightmap = room.getString("heightmap");
        this.tradeState = RoomTradeState.valueOf(room.getString("trade_state"));

        this.kickState = RoomKickState.valueOf(room.getString("kick_state"));
        this.banState = RoomBanState.valueOf(room.getString("ban_state"));
        this.muteState = RoomMuteState.valueOf(room.getString("mute_state"));

        this.bubbleMode = room.getInt("bubble_mode");
        this.bubbleScroll = room.getInt("bubble_scroll");
        this.bubbleType = room.getInt("bubble_type");
        this.antiFloodSettings = room.getInt("flood_level");
        this.chatDistance = room.getInt("chat_distance");
    }

    public void save() {
        String tagString = "";

        for (int i = 0; i < tags.length; i++) {
            if (i != 0) {
                tagString += ",";
            }

            tagString += tags[i];
        }

        String decorString = "";

        for (Map.Entry<String, String> decoration : decorations.entrySet()) {
            decorString += decoration.getKey() + "=" + decoration.getValue() + ",";
        }

        if (CometSettings.roomPasswordEncryptionEnabled) {
            if (!this.password.equals(this.originalPassword)) {
                this.password = BCrypt.hashpw(this.password, BCrypt.gensalt(CometSettings.roomPasswordEncryptionRounds));
            }
        }

        RoomDao.updateRoom(id, name, description, ownerId, owner, category, maxUsers, access, password, score,
                tagString, decorString.equals("") ? "" : decorString.substring(0, decorString.length() - 1),
                model, hideWalls, thicknessWall, thicknessFloor, allowWalkthrough, allowPets, heightmap, tradeState,
                muteState, kickState, banState, bubbleMode, bubbleType, bubbleScroll, chatDistance, antiFloodSettings
        );
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public String getOwner() {
        return this.owner;
    }

    public Category getCategory() {
        Category category = CometManager.getNavigator().getCategory(this.category);

        if (category == null) {
            // TODO: Dummy category :p
        }

        return category;
    }

    public int getMaxUsers() {
        return this.maxUsers;
    }

    public String getAccess() {
        return this.access;
    }

    public String getPassword() {
        return this.password;
    }

    public int getScore() {
        return this.score;
    }

    public String[] getTags() {
        return this.tags;
    }

    public Map<String, String> getDecorations() {
        return this.decorations;
    }

    public String getModel() {
        return this.model;
    }

    public boolean getHideWalls() {
        return this.hideWalls;
    }

    public int getWallThickness() {
        return this.thicknessWall;
    }

    public int getFloorThickness() {
        return this.thicknessFloor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setDecorations(Map<String, String> decorations) {
        this.decorations = decorations;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setHideWalls(boolean hideWalls) {
        this.hideWalls = hideWalls;
    }

    public void setThicknessWall(int thicknessWall) {
        this.thicknessWall = thicknessWall;
    }

    public void setThicknessFloor(int thicknessFloor) {
        this.thicknessFloor = thicknessFloor;
    }

    public boolean getAllowWalkthrough() {
        return this.allowWalkthrough;
    }

    public void setAllowWalkthrough(boolean allowWalkthrough) {
        this.allowWalkthrough = allowWalkthrough;
    }

    public void setHeightmap(String heightmap) {
        this.heightmap = heightmap;
    }

    public String getHeightmap() {
        return this.heightmap;
    }

    public boolean isAllowPets() {
        return allowPets;
    }

    public void setAllowPets(boolean allowPets) {
        this.allowPets = allowPets;
    }

    public long getLastReferenced() {
        return lastReferenced;
    }

    public RoomData setLastReferenced(long lastReferenced) {
        this.lastReferenced = lastReferenced;

        return this;
    }

    public RoomTradeState getTradeState() {
        return tradeState;
    }

    public void setTradeState(RoomTradeState tradeState) {
        this.tradeState = tradeState;
    }

    public int getBubbleMode() {
        return bubbleMode;
    }

    public void setBubbleMode(int bubbleMode) {
        this.bubbleMode = bubbleMode;
    }

    public int getBubbleType() {
        return bubbleType;
    }

    public void setBubbleType(int bubbleType) {
        this.bubbleType = bubbleType;
    }

    public int getBubbleScroll() {
        return bubbleScroll;
    }

    public void setBubbleScroll(int bubbleScroll) {
        this.bubbleScroll = bubbleScroll;
    }

    public int getChatDistance() {
        return chatDistance;
    }

    public void setChatDistance(int chatDistance) {
        this.chatDistance = chatDistance;
    }

    public int getAntiFloodSettings() {
        return antiFloodSettings;
    }

    public void setAntiFloodSettings(int antiFloodSettings) {
        this.antiFloodSettings = antiFloodSettings;
    }

    public RoomMuteState getMuteState() {
        return muteState;
    }

    public void setMuteState(RoomMuteState muteState) {
        this.muteState = muteState;
    }

    public RoomKickState getKickState() {
        return kickState;
    }

    public void setKickState(RoomKickState kickState) {
        this.kickState = kickState;
    }

    public RoomBanState getBanState() {
        return banState;
    }

    public void setBanState(RoomBanState banState) {
        this.banState = banState;
    }
}
