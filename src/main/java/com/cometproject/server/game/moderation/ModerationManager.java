package com.cometproject.server.game.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.moderation.types.HelpTicket;
import com.cometproject.server.storage.queries.moderation.PresetDao;
import com.cometproject.server.storage.queries.moderation.TicketDao;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModerationManager {
    private List<String> userPresets;
    private List<String> roomPresets;
    private Map<Integer, HelpTicket> tickets;

    private Logger logger = Logger.getLogger(ModerationManager.class.getName());

    public ModerationManager() {
        loadPresets();
        loadActiveTickets();
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

        try {
            PresetDao.getPresets(userPresets, roomPresets);

            logger.info("Loaded " + (this.getRoomPresets().size() + this.getUserPresets().size()) + " moderation presets");
        } catch (Exception e) {
            logger.error("Error while loading moderation presets", e);
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
            logger.info("Loaded " + this.tickets.size() + " active help tickets");
        } catch (Exception e) {
            logger.error("Error while loading active tickets", e);
        }
    }

    public void addTicket(HelpTicket ticket) {
        this.tickets.put(ticket.getId(), ticket);

        // TODO: send ticket to all moderators.
        /*synchronized (Comet.getServer().getNetwork().getSessions().getSessions()) {
            for (Session session : Comet.getServer().getNetwork().getSessions().getSessions().values()) {
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
            if (ticket.getPlayerId() == id)
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
}
