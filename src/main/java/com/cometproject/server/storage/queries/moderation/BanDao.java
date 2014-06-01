package com.cometproject.server.storage.queries.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.moderation.types.Ban;
import com.cometproject.server.storage.SqlHelper;
import javolution.util.FastMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class BanDao {
    public static Map<String, Ban> getActiveBans() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        FastMap<String, Ban> data = new FastMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM bans WHERE expire = 0 OR expire > " + Comet.getTime(), sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getString("data"), new Ban(resultSet));
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

    public static int createBan(long length, long expire, String data) {

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into bans (`type`, `expire`, `data`, `reason`) VALUES(?, ?, ?, ?);", sqlConnection, true);

            preparedStatement.setString(1, "user");
            preparedStatement.setLong(2, length == 0 ? 0 : expire);
            preparedStatement.setString(3, data);
            preparedStatement.setString(4, "");

            SqlHelper.executeStatementSilently(preparedStatement, false);
            resultSet = preparedStatement.getGeneratedKeys();

            while(resultSet.next()) {
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
}
