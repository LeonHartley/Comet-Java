package com.cometproject.server.game.groups.types;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMember {
    private int userId;
    private int groupId;
    private int accessLevel;

    public GroupMember(ResultSet data) throws SQLException {
        this.userId = data.getInt("user_id");
        this.accessLevel = data.getInt("rank"); // 0, 1, 2
        this.groupId = data.getInt("group_id");
    }

    public int getUserId() {
        return this.userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getAccessLevel() {
        return this.accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void save() {
        /*PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE group_members SET rank = ? WHERE user_id = ? AND group_id = ?");
        statement.setInt(1, accessLevel);
        statement.setInt(2, userId);
        statement.setInt(3, groupId);

        statement.execute();*/
    }
}
