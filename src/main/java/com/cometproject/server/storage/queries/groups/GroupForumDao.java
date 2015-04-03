package com.cometproject.server.storage.queries.groups;

import com.cometproject.server.game.groups.types.components.forum.settings.ForumPermission;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumSettings;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupForumDao {
    public static ForumSettings createSettings(int groupId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT INTO group_forum_settings (`group_id`) VALUES(?);", sqlConnection, true);

            preparedStatement.setInt(1, groupId);

            preparedStatement.execute();
         } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return new ForumSettings(groupId, ForumPermission.EVERYBODY, ForumPermission.EVERYBODY, ForumPermission.EVERYBODY,
                ForumPermission.ADMINISTRATORS);
    }

    public static ForumSettings getSettings(int groupId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM group_forum_settings WHERE group_id = ?", sqlConnection);
            preparedStatement.setInt(1, groupId);

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                return new ForumSettings(
                        groupId,
                        ForumPermission.valueOf(resultSet.getString("read_permission")),
                        ForumPermission.valueOf(resultSet.getString("post_permission")),
                        ForumPermission.valueOf(resultSet.getString("thread_permission")),
                        ForumPermission.valueOf(resultSet.getString("moderate_permission"))
                );
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return null;
    }
}
