package com.cometproject.server.game.wired.data;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.storage.queries.items.WiredDao;
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
        if (instances.containsKey(item.getId())) {
            return instances.get(item.getId());
        }

        try {
            WiredDataInstance instance = WiredDao.getDataByItemId(item.getId());

            if (instance == null) {
                instance = create(item.getDefinition().getInteraction(), item.getId(), "");
            }

            instances.put(instance.getItemId(), instance);

            return instance;
        } catch (Exception e) {
            log.error("Error while attempting to load wired data for item: " + item.getId(), e);
        }

        return null;
    }

    public static WiredDataInstance create(String wiredType, int itemId, String data) {
        int instanceId = WiredDao.createWiredData(wiredType, itemId, data);

        WiredDataInstance instance = buildInstance(itemId, data, instanceId);
        instances.put(itemId, instance);

        return instance;
    }

    public static WiredDataInstance buildInstance(int itemId, String data, int instanceId) {
        return new WiredDataInstance(instanceId, itemId, data);
    }

    public static void save(WiredDataInstance data) {
        String saveData = data.getDelay() + ":" + data.getMovement() + ":" + data.getRotation() + ":";

        if (data.getItems().size() != 0) {
            int last = data.getItems().get(data.getItems().size() - 1);

            for (int id : data.getItems()) {
                if (id != last) {
                    saveData += id + ",";
                } else {
                    saveData += id;
                }
            }
        }

        WiredDao.saveWiredData(data.getItemId(), saveData);
    }

    public static void removeInstance(int id) {
        instances.remove(id);
    }
}
