package com.cometproject.server.game.rooms.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.navigator.types.Category;
import javolution.util.FastMap;

import java.sql.PreparedStatement;
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

    private int score;

    private String[] tags;
    private Map<String, String> decorations;

    private String model;

    private boolean hideWalls;
    private int thicknessWall;
    private int thicknessFloor;


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
    }

    public void save() throws SQLException {
        PreparedStatement std = Comet.getServer().getStorage().prepare("UPDATE rooms SET name = ?, description = ?, owner_id = ?, owner = ?, category = ?, max_users = ?, access_type = ?, password = ?, score = ?, tags = ?, " +
                "decorations = ?, model = ?, hide_walls = ?, thickness_wall = ?, thickness_floor = ? WHERE id = ?");

        std.setString(1, name);
        std.setString(2, description);
        std.setInt(3, ownerId);
        std.setString(4, owner);
        std.setInt(5, category);
        std.setInt(6, maxUsers);
        std.setString(7, access);
        std.setString(8, password);
        std.setInt(9, score);

        String tagString = "";

        for (int i = 0; i < tags.length; i++) {
            if (i != 0) {
                tagString += ",";
            }

            tagString += tags[i];
        }

        std.setString(10, tagString);

        String decorString = "";

        for (Map.Entry<String, String> decoration : decorations.entrySet()) {
            decorString += decoration.getKey() + "=" + decoration.getValue() + ",";
        }

        System.out.println(decorString.substring(0, decorString.length() - 1));

        std.setString(11, decorString.substring(0, decorString.length() - 1));
        std.setString(12, model);
        std.setString(13, hideWalls ? "1" : "0");
        std.setInt(14, thicknessWall);
        std.setInt(15, thicknessFloor);
        std.setInt(16, id);

        std.executeUpdate();
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
        Category category = GameEngine.getNavigator().getCategory(this.category);

        if(category == null) {

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

}
