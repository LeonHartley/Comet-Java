package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.game.wired.misc.WiredSquare;
import com.cometproject.server.game.wired.types.TriggerType;
import com.cometproject.server.network.messages.types.Event;
import javolution.util.FastSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WiredComponent {
    private Room room;

    private FastSet<WiredSquare> squares;

    public WiredComponent(Room room) {
        this.room = room;

        this.squares = new FastSet<>();
    }

    public void dispose() {
        this.squares.clear();
        this.squares = null;
        this.room = null;
    }

    public boolean isWiredSquare(int x, int y) {
        for (WiredSquare square : this.squares) {
            if (square.getX() == x && square.getY() == y) {
                return true;
            }
        }

        return false;
    }

    public boolean trigger(TriggerType type, Object data, GenericEntity entity) {
        List<GenericEntity> entities = new ArrayList<>();
        entities.add(entity);

        return trigger(type, data, entities);
    }

    public boolean trigger(TriggerType type, Object data, List<GenericEntity> entities) {
        if (this.squares.size() == 0) {
            return false;
        }

        boolean wasTriggered = false;

        try {
            for (WiredSquare s : this.squares) {
                for (RoomItemFloor item : this.getRoom().getItems().getItemsOnSquare(s.getX(), s.getY())) {
                    if (CometManager.getWired().isWiredTrigger(item)) {
                        if (item.getDefinition().getInteraction().equals(CometManager.getWired().getString(type))) {
                            if (type == TriggerType.ON_SAY) {
                                if (!item.getExtraData().equals(data)) {
                                    continue;
                                }
                            } else if (type == TriggerType.ON_FURNI) {
                                WiredDataInstance wiredData = WiredDataFactory.get(item);
                                int itemId = (int) data;

                                if (!wiredData.getItems().contains(itemId)) {
                                    continue;
                                }
                            }

                            CometManager.getWired().getTrigger(item.getDefinition().getInteraction()).onTrigger(data, entities, s);
                            wasTriggered = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            this.room.log.error("Error while processing wired trigger", e);
        }

        return wasTriggered;
    }

    public void tick() {
        if (this.squares == null)
            return;

        for (WiredSquare square : this.squares) {
            boolean hasTimer = false;

            for (RoomItemFloor item : this.getRoom().getItems().getItemsOnSquare(square.getX(), square.getY())) {
                if (CometManager.getWired().isWiredTrigger(item)) {
                    if (item.getDefinition().getInteraction().equals("wf_trg_timer")) {
                        if (hasTimer)
                            continue;

                        hasTimer = true;
                        WiredDataInstance data = WiredDataFactory.get(item);

                        if (data.cycles >= data.getDelay()) {
                            List<GenericEntity> entities = this.getRoom().getEntities().getEntitiesCollection().values().stream().collect(Collectors.toList());

                            this.getRoom().getWired().trigger(TriggerType.TIMER, null, entities);
                            data.cycles = 0;
                        }

                        data.cycles++;
                    }
                }
            }
        }
    }

    public void handleSave(RoomItemFloor item, Event msg) {
        if (item.getDefinition().getInteraction().startsWith("wf_trg_")) {
            CometManager.getWired().getTrigger(item.getDefinition().getInteraction()).onSave(msg, item);

        } else if (item.getDefinition().getInteraction().startsWith("wf_act_")) {
            CometManager.getWired().getEffect(item.getDefinition().getInteraction()).onSave(msg, item);
        }
    }

    public void disposeSquare(WiredSquare square) {
        this.squares.remove(square);
    }

    public FastSet<WiredSquare> getSquares() {
        return this.squares.shared();
    }

    public void add(int x, int y) {
        this.squares.add(new WiredSquare(x, y));
    }

    public Room getRoom() {
        return this.room;
    }
}
