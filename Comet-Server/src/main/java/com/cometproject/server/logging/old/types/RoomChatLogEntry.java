//package com.cometproject.server.logging.old.types;

//
//import com.cometproject.server.logging.old.AbstractLogEntry;
//import com.cometproject.server.logging.old.LogType;


//
//public class RoomChatLogEntry extends AbstractLogEntry {
//
//    private int roomId;
//    private int userId;
//    private String message;
//    private String timestamp;
//
//    public RoomChatLogEntry(int roomId, int userId, String message) {
//        this.roomId = roomId;
//        this.userId = userId;
//        this.message = message;
//        this.timestamp = String.valueOf((int) (System.currentTimeMillis() / 1000L));
//    }
//
//    @Override
//    public LogType getType() {
//        return LogType.ROOM_CHATLOGS;
//    }
//
//    @Override
//    public String getString() {
//        return this.message;
//    }
//
//    @Override
//    public String getTimestamp() {
//        return this.timestamp;
//    }
//
//    @Override
//    public int getRoomId() {
//        return this.roomId;
//    }
//
//    @Override
//    public int getPlayerId() {
//        return this.userId;
//    }
//}
