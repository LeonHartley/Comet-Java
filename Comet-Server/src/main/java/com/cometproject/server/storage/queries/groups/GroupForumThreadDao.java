package com.cometproject.server.storage.queries.groups;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThreadReply;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroupForumThreadDao {
    public static Map<Integer, ForumThread> getAllMessagesForGroup(int groupId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, ForumThread> threads = new ConcurrentHashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM group_forum_messages WHERE group_id = ? ORDER BY FIELD(type, 'THREAD', 'REPLY'), author_timestamp DESC;", sqlConnection);
            preparedStatement.setInt(1, groupId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                switch (resultSet.getString("type")) {
                    case "THREAD":
                        final ForumThread forumThread = new ForumThread(resultSet.getInt("id"),
                                resultSet.getString("title"), resultSet.getString("message"),
                                resultSet.getInt("author_id"), resultSet.getInt("author_timestamp"),
                                resultSet.getInt("state"), resultSet.getString("locked").equals("1"),
                                resultSet.getString("hidden").equals("1"));

                        threads.put(forumThread.getId(), forumThread);
                        break;

                    case "REPLY":
                        final ForumThreadReply threadReply = new ForumThreadReply(resultSet.getInt("id"),
                                resultSet.getString("message"), resultSet.getInt("author_id"),
                                resultSet.getInt("author_timestamp"), resultSet.getString("hidden").equals("1"));

                        if(!threads.containsKey(threadReply.getThreadId())) {
                            continue;
                        }

                        threads.get(threadReply.getThreadId()).addReply(threadReply);
                        break;
                }
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return threads;
    }

    public ForumThread createThread(int groupId, String title, String message, int authorId) {
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

            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                return new ForumThread(resultSet.getInt(0), title, message, authorId, time, 0, false, false);
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
}
