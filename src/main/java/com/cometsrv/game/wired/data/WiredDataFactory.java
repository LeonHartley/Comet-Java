package com.cometsrv.game.wired.data;

import com.cometsrv.boot.Comet;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class WiredDataFactory {
    private static Map<Integer, WiredDataInstance> instances;
    private static Logger log = Logger.getLogger(WiredDataFactory.class.getName());

    public static void init() {
        instances = new FastMap<>();
    }

    public static WiredDataInstance get(Integer id) {
        if(instances.containsKey(id)) {
            return instances.get(id);
        }

        try {
            ResultSet data = Comet.getServer().getStorage().getRow("SELECT * FROM items_wired_data WHERE item_id = " + id);

            if(data == null) {
                // TODO: create new instance
                return null;
            }
        } catch(Exception e) {
            log.error("Error while attempting to load wired data for item: " + id, e);
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

            }

        } catch(Exception e) {
            log.error("Error while creating inserting wired data for item: " + itemId, e);
        }
    }

    public static void save(WiredDataInstance data) {
        throw new NotImplementedException();
    }
}
