package com.cometproject.server.game.groups.types;

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
    private int state; // TODO: set this to enum
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
        this.state = data.getInt("state");
        this.colourA = data.getInt("colour1");
        this.colourB = data.getInt("colour2");
        this.adminDeco = data.getString("admindeco").equals("1");
    }

    public void save() {

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

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public boolean isAdminDeco() {
        return adminDeco;
    }

    public void setAdminDeco(boolean adminDeco) {
        this.adminDeco = adminDeco;
    }
}
