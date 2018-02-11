package com.cometproject.server.game.rooms.types;

import com.cometproject.api.game.rooms.RoomType;
import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.game.rooms.RoomCategory;
import com.cometproject.api.game.rooms.settings.*;
import com.cometproject.server.boot.Comet;
import com.cometproject.api.config.CometSettings;
import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.storage.queries.rooms.RoomDao;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Map;


public class RoomData implements IRoomData {
    private int id;
    private RoomType type;

    private String name;
    private String description;
    private int ownerId;
    private String owner;
    private int category;
    private int maxUsers;
    private RoomAccessType access;
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

    private List<String> disabledCommands;

    private int groupId;

    private long lastReferenced = Comet.getTime();

    private String requiredBadge;
    private String thumbnail;

    private boolean wiredHidden;

    public RoomData(int id, RoomType type, String name, String description, int ownerId, String owner, int category,
                    int maxUsers, RoomAccessType access, String password, String originalPassword,
                    RoomTradeState tradeState, int score, String[] tags, Map<String, String> decorations,
                    String model, boolean hideWalls, int thicknessWall, int thicknessFloor, boolean allowWalkthrough,
                    boolean allowPets, String heightmap, RoomMuteState muteState, RoomKickState kickState,
                    RoomBanState banState, int bubbleMode, int bubbleType, int bubbleScroll, int chatDistance,
                    int antiFloodSettings, List<String> disabledCommands, int groupId, long lastReferenced,
                    String requiredBadge, String thumbnail, boolean wiredHidden) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.owner = owner;
        this.category = category;
        this.maxUsers = maxUsers;
        this.access = access;
        this.password = password;
        this.originalPassword = originalPassword;
        this.tradeState = tradeState;
        this.score = score;
        this.tags = tags;
        this.decorations = decorations;
        this.model = model;
        this.hideWalls = hideWalls;
        this.thicknessWall = thicknessWall;
        this.thicknessFloor = thicknessFloor;
        this.allowWalkthrough = allowWalkthrough;
        this.allowPets = allowPets;
        this.heightmap = heightmap;
        this.muteState = muteState;
        this.kickState = kickState;
        this.banState = banState;
        this.bubbleMode = bubbleMode;
        this.bubbleType = bubbleType;
        this.bubbleScroll = bubbleScroll;
        this.chatDistance = chatDistance;
        this.antiFloodSettings = antiFloodSettings;
        this.disabledCommands = disabledCommands;
        this.groupId = groupId;
        this.lastReferenced = lastReferenced;
        this.requiredBadge = requiredBadge;
        this.thumbnail = thumbnail;
        this.wiredHidden = wiredHidden;
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

        if (CometSettings.roomEncryptPasswords) {
            if (!this.password.equals(this.originalPassword)) {
                this.password = BCrypt.hashpw(this.password, BCrypt.gensalt(CometSettings.roomPasswordEncryptionRounds));
            }
        }

        RoomDao.updateRoom(id, name, StringUtils.abbreviate(description, 255), ownerId, owner, category, maxUsers, access, password, score,
                tagString, decorString.equals("") ? "" : decorString.substring(0, decorString.length() - 1),
                model, hideWalls, thicknessWall, thicknessFloor, allowWalkthrough, allowPets, heightmap, tradeState,
                muteState, kickState, banState, bubbleMode, bubbleType, bubbleScroll, chatDistance, antiFloodSettings,
                this.disabledCommands.isEmpty() ? "" : StringUtils.join(this.disabledCommands, ","), this.groupId, this.requiredBadge, this.thumbnail, this.wiredHidden
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

    public RoomCategory getCategory() {
        return NavigatorManager.getInstance().getCategory(this.category);
    }

    public int getMaxUsers() {
        return this.maxUsers;
    }

    public RoomAccessType getAccess() {
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

    public void setAccess(RoomAccessType access) {
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

    @Override
    public List<String> getDisabledCommands() {
        return this.disabledCommands;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public String getDecorationString() {
        String decorString = "";

        for (Map.Entry<String, String> decoration : decorations.entrySet()) {
            decorString += decoration.getKey() + "=" + decoration.getValue() + ",";
        }

        return decorString;
    }

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getRequiredBadge() {
        return this.requiredBadge;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean isWiredHidden() {
        return this.wiredHidden;
    }

    @Override
    public void setIsWiredHidden(boolean hiddenWired) {
        this.wiredHidden = hiddenWired;
    }
}
