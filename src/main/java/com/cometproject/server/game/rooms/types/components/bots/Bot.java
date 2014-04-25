package com.cometproject.server.game.rooms.types.components.bots;

import com.cometproject.server.game.players.components.types.InventoryBot;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.types.Composer;
import javolution.util.FastMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public abstract class Bot {
    private int id, ownerId;
    private String owner, name, figure, gender, motto;

    private int virtualId, x, y, bodyRotation, headRotation;
    private double z;
    private FastMap<String, String> statuses = new FastMap<>();
    private boolean needsRemove = false;
    private Room room;

    public Bot(int id, int ownerId, String owner, String name, String figure, String gender, String motto, int virtualId, int x, int y, double z, Room room) {
        this.id = id;
        this.ownerId = ownerId;
        this.owner = owner;
        this.name = name;
        this.figure = figure;
        this.gender = gender;
        this.motto = motto;
        this.virtualId = virtualId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.room = room;
    }

    public Bot(int virtualId, ResultSet data, Room room) throws SQLException {
        this.id = data.getInt("id");
        this.virtualId = virtualId;
        this.ownerId = data.getInt("owner_id");
        this.owner = data.getString("owner");

        this.name = data.getString("name");
        this.figure = data.getString("figure");
        this.gender = data.getString("gender");
        this.motto = data.getString("motto");

        this.x = data.getInt("x");
        this.y = data.getInt("y");
        this.z = data.getDouble("z");
        this.room = room;
    }

    public Bot(int virtualId, InventoryBot bot, int x, int y, Room room) {
        this.id = bot.getId();
        this.virtualId = virtualId;
        this.ownerId = bot.getOwnerId();

        this.name = bot.getName();
        this.figure = bot.getFigure();
        this.gender = bot.getGender();
        this.motto = bot.getMotto();

        this.x = x;
        this.y = y;
        this.z = 0.0;
        this.room = room;
    }

    public abstract void tick();

    public void setNeedsRemove(boolean needsRemove) {
        this.needsRemove = needsRemove;
    }

    public boolean needsRemove() {
        return this.needsRemove;
    }

    public Map<String, String> getStatuses() {
        return this.statuses;
    }

    public int getBodyRotation() {
        return this.bodyRotation;
    }

    public int getHeadRotation() {
        return this.headRotation;
    }

    public int getId() {
        return this.id;
    }

    public int getVirtualId() {
        return this.virtualId;
    }

    public String getOwner() {
        return this.owner;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public String getName() {
        return this.name;
    }

    public String getFigure() {
        return this.figure;
    }

    public String getGender() {
        return this.gender;
    }

    public String getMotto() {
        return this.motto;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public Room getRoom() {
        return this.room;
    }
}
