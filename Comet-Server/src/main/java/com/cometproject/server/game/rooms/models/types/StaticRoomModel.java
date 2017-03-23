package com.cometproject.server.game.rooms.models.types;

import com.cometproject.server.game.rooms.models.RoomModel;

import java.sql.ResultSet;


public class StaticRoomModel extends RoomModel {
    public StaticRoomModel(ResultSet data) throws Exception {
        super(data.getString("id"), data.getString("heightmap"), data.getInt("door_x"), data.getInt("door_y"), data.getInt("door_dir"), -1);
    }
}
