package com.cometproject.server.storage.queries.groups;

import com.cometproject.api.game.groups.types.components.forum.IForumThread;
import com.cometproject.api.game.groups.types.components.forum.IForumThreadReply;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.storage.SqlHelper;
import com.cometproject.storage.mysql.models.factories.GroupForumMessageFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupForumThreadDao {
    public static IForumThread createThread(int groupId, String title, String message, int authorId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        final int time = (int) Comet.getTime();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into group_forum_messages (type, group_id, title, message, author_id, author_timestamp) VALUES('THREAD', ?, ?, ?, ?, ?);", sqlConnection, true);

            preparedStatement.setInt(1, groupId);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, message);
            preparedStatement.setInt(4, authorId);
            preparedStatement.setInt(5, time);

            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                return new GroupForumMessageFactory().createThread(resultSet.getInt(1), title, message, authorId, time, 1, false, false, 0, "");
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

    public static IForumThreadReply createReply(int groupId, int threadId, String message, int authorId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        final int time = (int) Comet.getTime();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into group_forum_messages (type, group_id, thread_id, message, " +
                    "author_id, author_timestamp) VALUES('REPLY', ?, ?, ?, ?, ?);", sqlConnection, true);

            preparedStatement.setInt(1, groupId);
            preparedStatement.setInt(2, threadId);
            preparedStatement.setString(3, message);
            preparedStatement.setInt(4, authorId);
            preparedStatement.setInt(5, time);

            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                return new GroupForumMessageFactory().createThreadReply(resultSet.getInt(1), -1, message, threadId, authorId, time, 1, 0, "");
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

    public static void saveMessageState(int messageId, int state, int playerId, String username) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE group_forum_messages SET state = ?, moderator_id = ?, moderator_username = ? WHERE id = ?", sqlConnection);

            preparedStatement.setInt(1, state);
            preparedStatement.setInt(2, playerId);
            preparedStatement.setString(3, username);
            preparedStatement.setInt(4, messageId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveMessageLockState(int messageId, boolean isLocked, int playerId, String username) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE group_forum_messages SET locked = ?, moderator_id = ?, moderator_username = ? WHERE id = ?", sqlConnection);

            preparedStatement.setString(1, isLocked ? "1" : "0");
            preparedStatement.setInt(2, playerId);
            preparedStatement.setString(3, username);
            preparedStatement.setInt(4, messageId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveMessagePinnedState(int messageId, boolean isPinned) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE group_forum_messages SET pinned = ? WHERE id = ?", sqlConnection);

            preparedStatement.setString(1, isPinned ? "1" : "0");
            preparedStatement.setInt(2, messageId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static int getPlayerMessageCount(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT COUNT(0) as messageCount FROM group_forum_messages WHERE author_id = ?", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getInt("messageCount");
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return 0;
    }
}
