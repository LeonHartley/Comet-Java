package com.cometproject.server.game.rooms.backup;

import com.cometproject.server.game.rooms.types.Room;
import org.apache.log4j.Logger;

import java.util.Map;

public class RoomBackupFactory {
    private Map<String, RoomBackupInstance> globalBackups;
    private Logger log = Logger.getLogger(RoomBackupFactory.class.getName());

    public static boolean make(String name, Room room) {
        return false;
    }

    public static boolean restore(String backupId, Room room) {
        return false;
    }
}
