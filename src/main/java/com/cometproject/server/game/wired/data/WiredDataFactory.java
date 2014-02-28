package com.cometproject.server.game.wired.data;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.items.FloorItem;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class WiredDataFactory {
    private static Map<Integer, WiredDataInstance> instances;
    private static Logger log = Logger.getLogger(WiredDataFactory.class.getName());

    public static void init() {
        instances = new FastMap<>();
    }

    public static WiredDataInstance get(FloorItem item) {
        if(instances.containsKey(item.getId())) {
            return instances.get(item.getId());
        }

        try {
            ResultSet data = Comet.getServer().getStorage().getRow("SELECT * FROM items_wired_data WHERE item_id = " + item.getId());

            if(data == null) {
                WiredDataInstance instance = create(item.getDefinition().getInteraction(), item.getId(), "");
                instances.put(item.getId(), instance);
                return instance;
            }

            WiredDataInstance instance = buildInstance(data.getInt("item_id"), data.getString("data"), data.getInt("id"));
            instances.put(instance.getItemId(), instance);

            return instance;
        } catch(Exception e) {
            log.error("Error while attempting to load wired data for item: " + item.getId(), e);
        }

        return null;
    }

    public static WiredDataInstance create(String wiredType, int itemId, String data) {
        try {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into items_wired_data VALUES(null, ?, ?, ?);", true);

            statement.setInt(1, itemId);
            statement.setString(2, wiredType);
            statement.setString(3, data);

            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()) {
                int insertedId = keys.getInt(1);

                WiredDataInstance instance = buildInstance(itemId, data, insertedId);
                instances.put(itemId, instance);

                return instance;
            }

        } catch(Exception e) {
            log.error("Error while creating inserting wired data for item: " + itemId, e);
        }

        return null;
    }

    private static WiredDataInstance buildInstance(int itemId, String data, int instanceId) {
        return new WiredDataInstance(instanceId, itemId, data);
    }

    public static void save(WiredDataInstance data) {
        String saveData = data.getDelay() + ":" + data.getMovement() + ":" + data.getRotation() + ":";

        if(data.getItems().size() != 0) {
            int last = data.getItems().get(data.getItems().size() - 1);

            for(int id : data.getItems()) {
                if(id != last) {
                    saveData += id + ",";
                } else {
                    saveData += id;
                }
            }
        }

        try {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE items_wired_data SET data = ? WHERE id = ?");

            statement.setString(1, saveData);
            statement.setInt(2, data.getId());

            statement.executeUpdate();
        } catch(Exception e) {
            log.error("Error while updating wired data", e);
        }
    }

    public static void removeInstance(int id) {
        instances.remove(id);
    }
}
