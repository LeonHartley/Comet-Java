package com.cometproject.server.storage.queries.moderation;

import com.cometproject.server.game.moderation.types.tickets.HelpTicket;
import com.cometproject.server.game.moderation.types.tickets.HelpTicketState;
import com.cometproject.server.game.rooms.types.components.types.ChatMessage;
import com.cometproject.server.storage.SqlHelper;
import com.cometproject.server.utilities.JsonFactory;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javolution.util.FastMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TicketDao {
    public static Map<Integer, HelpTicket> getOpenTickets() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, HelpTicket> data = new FastMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM moderation_help_tickets WHERE state = 'OPEN'", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                List<ChatMessage> chatMessages;
                String chatMessageString = resultSet.getString("chat_messages");

                if (chatMessageString == null || chatMessageString.isEmpty()) {
                    chatMessages = Lists.newArrayList();
                } else {
                    chatMessages = JsonFactory.getInstance().fromJson(chatMessageString, new TypeToken<ArrayList<ChatMessage>>() {
                    }.getType());
                }

                data.put(resultSet.getInt("id"), new HelpTicket(resultSet.getInt("id"), resultSet.getInt("room_id"), resultSet.getInt("opener_id"), resultSet.getInt("reported_id"), resultSet.getInt("moderator_id"),
                        resultSet.getInt("category_id"), HelpTicketState.valueOf(resultSet.getString("state")), chatMessages));
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
