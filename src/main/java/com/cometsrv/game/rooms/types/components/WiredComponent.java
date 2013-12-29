package com.cometsrv.game.rooms.types.components;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.wired.misc.WiredSquare;
import com.cometsrv.game.wired.types.TriggerType;
import com.cometsrv.network.messages.types.Event;
import javolution.util.FastList;

public class WiredComponent {
    private Room room;

    private FastList<WiredSquare> squares;

    public WiredComponent(Room room) {
        this.room = room;

        this.squares = new FastList<>();
    }

    public void dispose() {
        this.squares.clear();
        this.squares = null;
        this.room = null;
    }

    public boolean isWiredSquare(int x, int y) {
        for(WiredSquare square : this.squares) {
            if(square.getX() == x && square.getY() == y) {
                return true;
            }
        }

        return false;
    }

    public boolean trigger(TriggerType type, Object data, Avatar user) {
        if(this.squares.size() == 0) {
            return false;
        }

        boolean wasTriggered = false;

        for(WiredSquare s : this.squares) {
            for(FloorItem item : this.getRoom().getItems().getItemsOnSquare(s.getX(), s.getY())) {
                if(GameEngine.getWired().isWiredTrigger(item)) {
                    if(item.getDefinition().getInteraction().equals(GameEngine.getWired().getString(type))) {
                        if(type == TriggerType.ON_SAY) {
                            if(!item.getExtraData().equals(data)) {
                                continue;
                            }
                        }

                        GameEngine.getWired().getTrigger(item.getDefinition().getInteraction()).onTrigger(data, user, s);
                        wasTriggered = true;
                    }
                }
            }
        }

        return wasTriggered;
    }

    public void handleSave(FloorItem item, Event msg) {
        if(item.getDefinition().getInteraction().startsWith("wf_trg_")) {
            GameEngine.getWired().getTrigger(item.getDefinition().getInteraction()).onSave(msg, item);

        } else if(item.getDefinition().getInteraction().startsWith("wf_act_")) {
            GameEngine.getWired().getEffect(item.getDefinition().getInteraction()).onSave(msg, item);
        }
    }

    public void add(int x, int y) {
        this.squares.add(new WiredSquare(x, y));
    }

    public Room getRoom() {
        return this.room;
    }
}
