package com.cometproject.server.game.players.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.outgoing.misc.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.RemoveObjectFromInventoryMessageComposer;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class InventoryComponent {
    private Player player;

    private Map<Integer, InventoryItem> floorItems;
    private Map<Integer, InventoryItem> wallItems;
    private Map<String, Integer> badges;

    private Logger log = Logger.getLogger(InventoryComponent.class.getName());

    public InventoryComponent(Player player) {
        this.player = player;

        this.floorItems = new FastMap<>();
        this.wallItems = new FastMap<>();
        this.badges = new FastMap<>();

        this.loadItems();
        this.loadBadges();
    }

    public void loadItems() {
        if(this.getWallItems().size() >= 1) {
            this.getWallItems().clear();
        }
        if(this.getFloorItems().size() >= 1) {
            this.getFloorItems().clear();
        }

        try {
            ResultSet data = Comet.getServer().getStorage().getTable("SELECT * FROM items WHERE user_id = " + this.getPlayer().getId() + " AND room_id = 0");

            while(data.next()) {
                InventoryItem item = new InventoryItem(data);

                if(item.getDefinition().getType().equals("s")) {
                    this.getFloorItems().put(item.getId(), item);
                }

                if(item.getDefinition().getType().equals("i")) {
                    this.getWallItems().put(item.getId(), item);
                }
            }
        } catch(Exception e) {
            log.error("Error while loading user inventory", e);
        }
    }

    public void loadBadges() {
        try {
            ResultSet data = Comet.getServer().getStorage().getTable("SELECT * FROM player_badges WHERE player_id = " + this.getPlayer().getId());

            while(data.next()) {
                if(!badges.containsKey(data.getString("badge_code")))
                    badges.put(data.getString("badge_code"), data.getInt("slot"));
            }
        } catch(Exception e) {
            log.error("Error while loading user badges");
        }
    }

    public void addBadge(String code, boolean insert) {
        if(!badges.containsKey(code)) {
            if(insert) {
                try {
                    PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into player_badges (`player_id`, `badge_code`) VALUES(?, ?)");

                    statement.setInt(1, this.getPlayer().getId());
                    statement.setString(2, code);
                } catch (SQLException e) {
                   log.error("Error while inserting badge to database");
                }
            }

            // 0 = slot
            this.badges.put(code, 0);
            this.player.getSession().send(AlertMessageComposer.compose(Locale.get("badge.get")));
        }
    }


    public void resetBadgeSlots() {
        for(Map.Entry<String, Integer> badge : this.badges.entrySet()) {
            if(badge.getValue() != 0) {
                this.badges.replace(badge.getKey(), 0);
            }

            System.out.println(badge.getKey() + ":" + badge.getValue());
        }
    }

    public void add(int id, int itemId, String extraData) {
        InventoryItem item = new InventoryItem(id, itemId, extraData);
        if(item.getDefinition().getType().equals("s")) {
            this.getFloorItems().put(id, item);
        }

        if(item.getDefinition().getType().equals("i")) {
            this.getWallItems().put(id, item);
        }
    }

    public void addItem(InventoryItem item) {
        if(item.getDefinition().getType().equals("s"))
            floorItems.put(item.getId(), item);
        else if(item.getDefinition().getType().equals("i"))
            wallItems.put(item.getId(), item);
    }

    public void removeItem(InventoryItem item) {
        if(item.getDefinition().getType().equals("s"))
            floorItems.remove(item.getId());
        else if(item.getDefinition().getType().equals("i"))
            wallItems.remove(item.getId());
    }

    public void removeFloorItem(int itemId) {
        this.getFloorItems().remove(itemId);
        this.getPlayer().getSession().send(RemoveObjectFromInventoryMessageComposer.compose(itemId));
    }

    public void removeWallItem(int itemId) {
        this.getWallItems().remove(itemId);
        this.getPlayer().getSession().send(RemoveObjectFromInventoryMessageComposer.compose(itemId));
    }

    public InventoryItem getFloorItem(int id) {
        return this.getFloorItems().get(id);
    }

    public InventoryItem getWallItem(int id) {
        return this.getWallItems().get(id);
    }

    public InventoryItem getItem(int id) {
        InventoryItem item = getFloorItem(id);

        if(item != null) {
            return item;
        }

        return getWallItem(id);
    }

    public void dispose() {
        this.getFloorItems().clear();
        this.floorItems = null;

        this.getWallItems().clear();
        this.wallItems = null;
    }

    public int getTotalSize() {
        return this.getWallItems().size() + this.getFloorItems().size();
    }

    public Map<Integer, InventoryItem> getWallItems() {
        return this.wallItems;
    }

    public Map<Integer, InventoryItem> getFloorItems() {
        return this.floorItems;
    }

    public Map<String, Integer> getBadges() {
        return this.badges;
    }

    public Player getPlayer() {
        return this.player;
    }
}
