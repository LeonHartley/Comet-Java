package com.cometsrv.game.navigator.types;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FeaturedRoom {
    private int id, roomId, categoryId, parentCategory;
    private String caption, description, image, image_type;
    private boolean enabled, recommended;

    public FeaturedRoom(ResultSet data) throws SQLException {
    }
}
