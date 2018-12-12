package com.cometproject.server.game.moderation;

import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.game.moderation.types.actions.ActionCategory;
import com.cometproject.server.game.moderation.types.tickets.HelpTicket;
import com.cometproject.server.game.moderation.types.tickets.HelpTicketState;
import com.cometproject.server.game.rooms.types.components.types.ChatMessage;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.moderation.tickets.HelpTicketMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.moderation.PresetDao;
import com.cometproject.server.storage.queries.moderation.TicketDao;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ModerationManager implements Initialisable {
    private static ModerationManager moderationManagerInstance;

    private List<String> userPresets;
    private List<String> roomPresets;
    private List<ActionCategory> actionCategories;

    private Map<Integer, HelpTicket> tickets;

    private ConcurrentHashSet<Session> moderators;
    private ConcurrentHashSet<Session> logChatUsers;

    private Logger log = LogManager.getLogger(ModerationManager.class.getName());

    public ModerationManager() {

    }

    public static ModerationManager getInstance() {
        if (moderationManagerInstance == null)
            moderationManagerInstance = new ModerationManager();

        return moderationManagerInstance;
    }

    @Override
    public void initialize() {
        this.moderators = new ConcurrentHashSet<>();
        this.logChatUsers = new ConcurrentHashSet<>();

        loadPresets();
        loadActiveTickets();

        log.info("ModerationManager initialized");
    }

    public void loadPresets() {
        if (userPresets == null) {
            userPresets = new ArrayList<>();
        } else {
            userPresets.clear();
        }

        if (roomPresets == null) {
            roomPresets = new ArrayList<>();
        } else {
            roomPresets.clear();
        }

        if (actionCategories == null) {
            actionCategories = new ArrayList<>();
        } else {
            for (ActionCategory actionCategory : actionCategories) {
                actionCategory.dispose();
            }

            actionCategories.clear();
        }

        try {
            PresetDao.getPresets(userPresets, roomPresets);
            PresetDao.getPresetActions(actionCategories);

            log.info("Loaded {} moderation presets", (this.getRoomPresets().size() + this.getUserPresets().size() + this.getActionCategories().size()));
        } catch (Exception e) {
            log.error("Error while loading moderation presets", e);
        }
    }

    public void addModerator(Session session) {
        this.moderators.add(session);
    }

    public void addLogChatUser(Session session) {
        this.logChatUsers.add(session);
    }

    public void removeLogChatUser(Session session) {
        this.logChatUsers.remove(session);
    }

    public void removeModerator(Session session) {
        this.moderators.remove(session);
    }

    public void loadActiveTickets() {
        if (tickets == null) {
            tickets = new HashMap<>();
        } else {
            tickets.clear();
        }

        try {
            this.tickets = TicketDao.getOpenTickets();
            log.info("Loaded {} active help tickets", this.tickets.size());
        } catch (Exception e) {
            log.error("Error while loading active tickets", e);
        }
    }

    private void addTicket(HelpTicket ticket) {
        this.tickets.put(ticket.getId(), ticket);
        this.broadcastTicket(ticket);
    }

    public void broadcastTicket(final HelpTicket ticket) {
        NetworkManager.getInstance().getSessions().broadcastToModerators(new HelpTicketMessageComposer(ticket));
    }

    public void createTicket(int submitterId, String message, int category, int reportedId, int timestamp, int roomId, List<ChatMessage> chatMessages) {
        int ticketId = TicketDao.createTicket(submitterId, message, category, reportedId, timestamp, roomId, chatMessages);

        final HelpTicket ticket = new HelpTicket(ticketId, category, timestamp, 0, submitterId, reportedId, 0, message, HelpTicketState.OPEN, chatMessages, roomId);
        this.addTicket(ticket);
    }

    public HelpTicket getTicket(int id) {
        return this.tickets.get(id);
    }

    public HelpTicket getTicketByUserId(int id) {
        for (HelpTicket ticket : tickets.values()) {
            if (ticket.getSubmitterId() == id)
                return ticket;
        }

        return null;
    }

    public List<String> getUserPresets() {
        return this.userPresets;
    }

    public List<String> getRoomPresets() {
        return this.roomPresets;
    }

    public List<ActionCategory> getActionCategories() {
        return this.actionCategories;
    }

    public HelpTicket getActiveTicketByPlayerId(int playerId) {
        HelpTicket ticket = this.getTicketByUserId(playerId);

        if (ticket != null) {
            if (ticket.getState() != HelpTicketState.CLOSED) {
                return ticket;
            }
        }

        return null;
    }

    public Map<Integer, HelpTicket> getTickets() {
        return tickets;
    }


    public ConcurrentHashSet<Session> getModerators() {
        return moderators;
    }

    public ConcurrentHashSet<Session> getLogChatUsers() {
        return logChatUsers;
    }
}
