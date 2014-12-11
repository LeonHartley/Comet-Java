package com.cometproject.server.game.moderation.types.tickets;

import com.cometproject.server.game.rooms.types.components.types.ChatMessage;

import java.util.List;


public class HelpTicket {
    private int ticketId;
    private int roomId;
    private int openerId;
    private int reportedId;
    private int moderatorId;
    private int categoryId;
    private HelpTicketState state;
    private List<ChatMessage> chatMessages;

    public HelpTicket(int ticketId, int roomId, int openerId, int reportedId, int moderatorId, int categoryId, HelpTicketState state, List<ChatMessage> chatMessages) {
        this.ticketId = ticketId;
        this.roomId = roomId;
        this.openerId = openerId;
        this.reportedId = reportedId;
        this.moderatorId = moderatorId;
        this.categoryId = categoryId;
        this.state = state;
        this.chatMessages = chatMessages;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getOpenerId() {
        return openerId;
    }

    public void setOpenerId(int openerId) {
        this.openerId = openerId;
    }

    public int getReportedId() {
        return reportedId;
    }

    public void setReportedId(int reportedId) {
        this.reportedId = reportedId;
    }

    public int getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public HelpTicketState getState() {
        return state;
    }

    public void setState(HelpTicketState state) {
        this.state = state;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }
}
