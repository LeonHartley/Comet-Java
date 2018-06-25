package com.cometproject.storage.api.data.rooms;

import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.game.rooms.RoomType;
import com.cometproject.api.game.rooms.settings.*;

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

    private String requiredBadge;
    private String thumbnail;

    private boolean wiredHidden;

    public RoomData(int id, RoomType type, String name, String description, int ownerId, String owner, int category,
                    int maxUsers, RoomAccessType access, String password, String originalPassword,
                    RoomTradeState tradeState, int score, String[] tags, Map<String, String> decorations,
                    String model, boolean hideWalls, int thicknessWall, int thicknessFloor, boolean allowWalkthrough,
                    boolean allowPets, String heightmap, RoomMuteState muteState, RoomKickState kickState,
                    RoomBanState banState, int bubbleMode, int bubbleType, int bubbleScroll, int chatDistance,
                    int antiFloodSettings, List<String> disabledCommands, int groupId,
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
        this.requiredBadge = requiredBadge;
        this.thumbnail = thumbnail;
        this.wiredHidden = wiredHidden;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getOwnerId() {
        return ownerId;
    }

    @Override
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public int getCategoryId() {
        return category;
    }

    @Override
    public void setCategoryId(int category) {
        this.category = category;
    }

    @Override
    public int getMaxUsers() {
        return maxUsers;
    }

    @Override
    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    @Override
    public RoomAccessType getAccess() {
        return access;
    }

    @Override
    public void setAccess(RoomAccessType access) {
        this.access = access;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getOriginalPassword() {
        return originalPassword;
    }

    @Override
    public void setOriginalPassword(String originalPassword) {
        this.originalPassword = originalPassword;
    }

    @Override
    public RoomTradeState getTradeState() {
        return tradeState;
    }

    @Override
    public void setTradeState(RoomTradeState tradeState) {
        this.tradeState = tradeState;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String[] getTags() {
        return tags;
    }

    @Override
    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Override
    public Map<String, String> getDecorations() {
        return decorations;
    }

    @Override
    public void setDecorations(Map<String, String> decorations) {
        this.decorations = decorations;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public boolean getHideWalls() {
        return hideWalls;
    }

    @Override
    public int getWallThickness() {
        return this.thicknessWall;
    }

    @Override
    public int getFloorThickness() {
        return this.thicknessFloor;
    }

    @Override
    public void setHideWalls(boolean hideWalls) {
        this.hideWalls = hideWalls;
    }

    @Override
    public void setThicknessWall(int thicknessWall) {
        this.thicknessWall = thicknessWall;
    }

    @Override
    public void setThicknessFloor(int thicknessFloor) {
        this.thicknessFloor = thicknessFloor;
    }

    @Override
    public boolean getAllowWalkthrough() {
        return this.allowWalkthrough;
    }

    @Override
    public boolean isAllowWalkthrough() {
        return allowWalkthrough;
    }

    @Override
    public void setAllowWalkthrough(boolean allowWalkthrough) {
        this.allowWalkthrough = allowWalkthrough;
    }

    @Override
    public boolean isAllowPets() {
        return allowPets;
    }

    @Override
    public void setAllowPets(boolean allowPets) {
        this.allowPets = allowPets;
    }

    @Override
    public String getHeightmap() {
        return heightmap;
    }

    @Override
    public void setHeightmap(String heightmap) {
        this.heightmap = heightmap;
    }

    @Override
    public RoomMuteState getMuteState() {
        return muteState;
    }

    @Override
    public void setMuteState(RoomMuteState muteState) {
        this.muteState = muteState;
    }

    @Override
    public RoomKickState getKickState() {
        return kickState;
    }

    @Override
    public void setKickState(RoomKickState kickState) {
        this.kickState = kickState;
    }

    @Override
    public RoomBanState getBanState() {
        return banState;
    }

    @Override
    public void setBanState(RoomBanState banState) {
        this.banState = banState;
    }

    @Override
    public int getBubbleMode() {
        return bubbleMode;
    }

    @Override
    public void setBubbleMode(int bubbleMode) {
        this.bubbleMode = bubbleMode;
    }

    @Override
    public int getBubbleType() {
        return bubbleType;
    }

    @Override
    public void setBubbleType(int bubbleType) {
        this.bubbleType = bubbleType;
    }

    @Override
    public int getBubbleScroll() {
        return bubbleScroll;
    }

    @Override
    public void setBubbleScroll(int bubbleScroll) {
        this.bubbleScroll = bubbleScroll;
    }

    @Override
    public int getChatDistance() {
        return chatDistance;
    }

    @Override
    public void setChatDistance(int chatDistance) {
        this.chatDistance = chatDistance;
    }

    @Override
    public int getAntiFloodSettings() {
        return antiFloodSettings;
    }

    @Override
    public void setAntiFloodSettings(int antiFloodSettings) {
        this.antiFloodSettings = antiFloodSettings;
    }

    @Override
    public List<String> getDisabledCommands() {
        return disabledCommands;
    }

    @Override
    public int getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getDecorationString() {
        StringBuilder decorString = new StringBuilder();

        for (Map.Entry<String, String> decoration : this.getDecorations().entrySet()) {
            decorString.append(decoration.getKey()).append("=").append(decoration.getValue()).append(",");
        }

        return decorString.toString();
    }

    @Override
    public String getRequiredBadge() {
        return requiredBadge;
    }

    @Override
    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean isWiredHidden() {
        return wiredHidden;
    }

    @Override
    public void setIsWiredHidden(boolean hiddenWired) {
        this.wiredHidden = hiddenWired;
    }
}
