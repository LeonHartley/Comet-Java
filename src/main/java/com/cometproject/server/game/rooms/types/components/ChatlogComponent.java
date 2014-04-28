package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.types.ChatMessage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatlogComponent {
    private List<ChatMessage> messages;
    private Room room;

    public ChatlogComponent(Room room) {
        this.messages = new ArrayList<>();
        this.room = room;
    }

    public void dispose() {
        this.messages.clear();
        this.messages = null;
        this.room = null;
    }

    public void add(String msg, int userId) {
        long time = System.currentTimeMillis();
        if (CometSettings.logChatToMemory) {
            this.messages.add(new ChatMessage(userId, msg, time));
        }
    }

    public void cycle() throws SQLException {
        if (!CometSettings.logChatToDatabase)
            return; // if we don't wanna log to db, we might as well halt right here...
/*
        Connection connection = Comet.getServer().getStorage().getConnections().getConnection();
        PreparedStatement statement;

        try {
            connection.setAutoCommit(false);

            statement = Comet.getServer().getStorage().prepare("INSERT into chatlogs (`message`, `user_id`) VALUES(?, ?)");

            for(ChatMessage message : messages) {
                statement.setString(1, message.getMessage());
                statement.setInt(2, message.getUserId());

                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();
        } catch(Exception e) {
            this.room.log.error("Error while saving chatlog", e);
            connection.rollback();
        } finally {
            connection.close();
        }
*/
        messages.clear();
    }
}
