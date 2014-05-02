package com.cometproject.server.storage.queries.player.relationships;

import com.cometproject.server.game.players.components.types.MessengerFriend;
import com.cometproject.server.game.players.components.types.RelationshipLevel;
import com.cometproject.server.storage.SqlHelper;
import javolution.util.FastMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class RelationshipDao {
    public static Map<Integer, RelationshipLevel> getRelationshipsByPlayerId(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, RelationshipLevel> data = new FastMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT `partner`, `level` FROM player_relationships WHERE player_id = ?", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getInt("partner"), RelationshipLevel.getLevel(resultSet.getString("level")));
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

    public static void deleteRelationship(int playerId, int partner) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("DELETE FROM player_relationships WHERE player_id = ? AND partner = ?", sqlConnection);
            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, partner);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
