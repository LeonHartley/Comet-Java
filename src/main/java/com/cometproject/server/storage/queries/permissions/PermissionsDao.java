package com.cometproject.server.storage.queries.permissions;

import com.cometproject.server.game.permissions.types.CommandPermission;
import com.cometproject.server.game.permissions.types.Perk;
import com.cometproject.server.game.permissions.types.Permission;
import com.cometproject.server.storage.SqlHelper;
import javolution.util.FastMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionsDao {

    public static FastMap<Integer, Perk> getPerks() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        FastMap<Integer, Perk> data = new FastMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT `id`, `title`, `data`, `override_rank`, `override_default`, `min_rank` FROM permission_perks", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                data.put(resultSet.getInt("id"), new Perk(resultSet));
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }

    public static FastMap<String, Permission> getRankPermissions() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        FastMap<String, Permission> data = new FastMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();
            preparedStatement = SqlHelper.prepare("SELECT `fuse`, `min_rank` FROM permission_ranks", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getString("fuse"), new Permission(resultSet));
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }

    public static FastMap<String, CommandPermission> getCommandPermissions() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        FastMap<String, CommandPermission> data = new FastMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();
            preparedStatement = SqlHelper.prepare("SELECT `command_id`, `minimum_rank`, `vip_only` FROM permission_commands", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getString("command_id"), new CommandPermission(resultSet));
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }
}
