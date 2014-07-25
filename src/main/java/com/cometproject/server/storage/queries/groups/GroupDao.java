package com.cometproject.server.storage.queries.groups;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDao {
    public static GroupData getDataById(int id) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM groups WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return new GroupData(resultSet);
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return null;
    }

    public static int create(GroupData groupData) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into groups (`name`, `description`, `badge`, `owner_id`, `room_id`, `created`, `type`, `colour1`, `colour2`, `admindeco`) " +
                    "VALUES(? ,?, ?, ?, ?, ?, ?, ?, ?, ?);", sqlConnection, true);
            preparedStatement.setString(1, groupData.getTitle());
            preparedStatement.setString(2, groupData.getDescription());
            preparedStatement.setString(3, groupData.getBadge());
            preparedStatement.setInt(4, groupData.getOwnerId());
            preparedStatement.setInt(5, groupData.getRoomId());
            preparedStatement.setInt(6, groupData.getCreatedTimestamp());
            preparedStatement.setString(7, groupData.getType().toString().toLowerCase());
            preparedStatement.setInt(8, groupData.getColourA());
            preparedStatement.setInt(9, groupData.getColourB());
            preparedStatement.setString(10, groupData.canAdminsDecorate() ? "1" : "0");

            SqlHelper.executeStatementSilently(preparedStatement, false);

            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return 0;
    }

    public static int getIdByRoomId(int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT `id` FROM `groups` WHERE `room_id` = ?", sqlConnection, true);
            preparedStatement.setInt(1, roomId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return 0;
    }

    public static void save(GroupData groupData) {
        if(groupData.getId() == -1) {
            CometManager.getLogger().warn("Tried to update group data which doesn't exist/doesn't have a valid ID (-1)! Title: " + groupData.getTitle());
            return;
        }

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE groups SET name = ?, description = ?, badge = ?, owner_id = ?, room_id = ?, type = ?, colour1 = ?, colour2 = ?, admindeco = ? WHERE id = ?", sqlConnection, true);
            preparedStatement.setString(1, groupData.getTitle());
            preparedStatement.setString(2, groupData.getDescription());
            preparedStatement.setString(3, groupData.getBadge());
            preparedStatement.setInt(4, groupData.getOwnerId());
            preparedStatement.setInt(5, groupData.getRoomId());
            preparedStatement.setString(6, groupData.getType().toString().toLowerCase());
            preparedStatement.setInt(7, groupData.getColourA());
            preparedStatement.setInt(8, groupData.getColourB());
            preparedStatement.setString(9, groupData.canAdminsDecorate() ? "1" : "0");
            preparedStatement.setInt(10, groupData.getId());

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

}
