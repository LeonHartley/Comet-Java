package com.cometproject.api.game.moderation;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.game.rooms.chat.IChatMessage;

import java.util.List;

public interface IHelpTicket {
    void save();

    void compose(IComposer msg);

    int getId();

    void setId(int id);

    int getDateSubmitted();

    void setDateSubmitted(int dateSubmitted);

    int getDateClosed();

    void setDateClosed(int dateClosed);

    int getSubmitterId();

    void setSubmitterId(int submitterId);

    int getReportedId();

    void setReportedId(int reportedId);

    int getModeratorId();

    void setModeratorId(int moderatorId);

    String getMessage();

    void setMessage(String message);

    HelpTicketState getState();

    void setState(HelpTicketState state);

    List<IChatMessage> getChatMessages();

    void setChatMessages(List<IChatMessage> chatMessages);

    int getCategoryId();

    void setCategoryId(int categoryId);

    int getRoomId();

    void setRoomId(int roomId);

    String getSubmitterUsername();

    String getReportedUsername();

    String getModeratorUsername();
}
