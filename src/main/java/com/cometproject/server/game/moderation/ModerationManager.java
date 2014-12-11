package com.cometproject.server.game.moderation;

import com.cometproject.server.game.moderation.types.actions.ActionCategory;
import com.cometproject.server.game.moderation.types.tickets.HelpTicket;
import com.cometproject.server.storage.queries.moderation.PresetDao;
import com.cometproject.server.storage.queries.moderation.TicketDao;
import com.cometproject.server.utilities.Initializable;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ModerationManager implements Initializable {
    private static ModerationManager moderationManagerInstance;

    private List<String> userPresets;
    private List<String> roomPresets;
    private List<ActionCategory> actionCategories;
    private Map<Integer, HelpTicket> tickets;

    private Logger log = Logger.getLogger(ModerationManager.class.getName());

    public ModerationManager() {

    }

    @Override
    public void initialize() {
        loadPresets();
        loadActiveTickets();

        log.info("ModerationManager initialized");
    }

    public static ModerationManager getInstance() {
        if (moderationManagerInstance == null)
            moderationManagerInstance = new ModerationManager();

        return moderationManagerInstance;
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

            log.info("Loaded " + (this.getRoomPresets().size() + this.getUserPresets().size()) + this.getActionCategories().size() + " moderation presets");
        } catch (Exception e) {
            log.error("Error while loading moderation presets", e);
        }
    }

    public void loadActiveTickets() {
        if (tickets == null) {
            tickets = new FastMap<>();
        } else {
            tickets.clear();
        }

        try {
            this.tickets = TicketDao.getOpenTickets();
            log.info("Loaded " + this.tickets.size() + " active help tickets");
        } catch (Exception e) {
            log.error("Error while loading active tickets", e);
        }
    }

    public void addTicket(HelpTicket ticket) {
        this.tickets.put(ticket.getTicketId(), ticket);

        // TODO: send ticket to all moderators.
        /*synchronized (NetworkManager.getInstance().getSessions().getSessions()) {
            for (Session session : NetworkManager.getInstance().getSessions().getSessions().values()) {
                if (session.getPlayer() != null) {
                    if (session.getPlayer().getPermissions().hasPermission("mod_tool")) {
                        session.send(HelpTicketMessageComposer.compose(ticket));
                    }
                }
            }
        }*/
    }

    public HelpTicket getTicket(int id) {
        return this.tickets.get(id);
    }

    public HelpTicket getTicketByUserId(int id) {
        for (HelpTicket ticket : tickets.values()) {
            if (ticket.getOpenerId() == id)
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
}
