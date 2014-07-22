package com.cometproject.server.game.groups.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.storage.queries.groups.GroupDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupData {
    private int id;
    private String title;
    private String description;
    private String badge;
    private int ownerId;
    private int roomId;
    private int created;
    private GroupType type;
    private int colourA;
    private int colourB;
    private boolean adminDeco;

    public GroupData(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.title = data.getString("name");
        this.description = data.getString("desc");
        this.badge = data.getString("badge");
        this.ownerId = data.getInt("owner_id");
        this.created = data.getInt("created");
        this.roomId = data.getInt("room_id");
        this.type = GroupType.valueOf(data.getString("type").toUpperCase());
        this.colourA = data.getInt("colour1");
        this.colourB = data.getInt("colour2");
        this.adminDeco = data.getString("admindeco").equals("1");
    }

    public GroupData(String title, String description, String badge, int ownerId, int roomId, int colourA, int colourB) {
        this.id = -1;
        this.title = title;
        this.description = description;
        this.badge = badge;
        this.ownerId = ownerId;
        this.roomId = roomId;
        this.created = (int) Comet.getTime();
        this.type = GroupType.REGULAR;
        this.colourA = colourA;
        this.colourB = colourB;
        this.adminDeco = false;
    }

    public void save() {
        GroupDao.save(this);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(int id) {
        this.ownerId = id;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getCreatedTimestamp() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public void setAdminDeco(boolean adminDeco) {
        this.adminDeco = adminDeco;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public int getColourA() {
        return colourA;
    }

    public void setColourA(int colourA) {
        this.colourA = colourA;
    }

    public int getColourB() {
        return colourB;
    }

    public void setColourB(int colourB) {
        this.colourB = colourB;
    }

    public boolean canAdminsDecorate() {
        return adminDeco;
    }
}
