package com.cometproject.server.game.rooms.items;

import com.cometproject.server.game.items.interactions.InteractionQueueItem;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.utilities.DistanceCalculator;
import com.cometproject.server.network.messages.types.Composer;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public abstract class RoomItem implements GenericRoomItem, InteractableRoomItem {
    private static final int MAX_INTERACTION_QUEUE = 1;

    protected int id;
    protected int itemId;
    protected int ownerId;

    protected int x;
    protected int y;

    protected int rotation;

    protected boolean state;

    private Queue<InteractionQueueItem> interactionQueue = new LinkedList<>();
    protected InteractionQueueItem curInteractionItem;

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getItemId() {
        return this.itemId;
    }

    @Override
    public int getOwner() {
        return this.ownerId;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getRotation() {
        return this.rotation;
    }

    @Override
    public boolean getState() {
        return this.state;
    }

    @Override
    public boolean hasInteraction() {
        return (this.curInteractionItem != null || this.interactionQueue.size() > 0);
    }

    @Override
    public InteractionQueueItem getNextInteraction() {
        if (this.curInteractionItem != null) {
            if (this.curInteractionItem.getUpdateCycles() > 0) {
                return this.curInteractionItem;
            } else {
                this.curInteractionItem = null;
            }
        }

        try {
            this.curInteractionItem = this.interactionQueue.remove();
        } catch (NoSuchElementException e) {
            this.curInteractionItem = null;
        }

        return this.curInteractionItem;
    }

    @Override
    public void queueInteraction(InteractionQueueItem interaction) {
        // check the queue size
        /*if (this.interactionQueue.size() > MAX_INTERACTION_QUEUE) {
            return; // ignore the interaction
        }*/

        this.interactionQueue.add(interaction);
    }

    public Queue<InteractionQueueItem> getInteractionQueue() {
        return this.interactionQueue;
    }

    public int distance(GenericEntity entity) {
        int avatarX = entity.getPosition().getX();
        int avatarY = entity.getPosition().getY();

        return DistanceCalculator.calculate(avatarX, avatarY, this.getX(), this.getY());
    }

    public boolean touching(GenericEntity entity) {
        int avatarX = entity.getPosition().getX();
        int avatarY = entity.getPosition().getY();

        return DistanceCalculator.tilesTouching(avatarX, avatarY, this.getX(), this.getY());
    }

    public Position3D squareInfront() {
        Position3D pos = new Position3D(getX(), getY(), 0);

        int posX = pos.getX();
        int posY = pos.getY();

        if(getRotation() == 0) {
            posY--;
        } else if(getRotation() == 2) {
            posX++;
        } else if(getRotation() == 4) {
            posY++;
        } else if(getRotation() == 6) {
            posX--;
        }

        pos.setX(posX);
        pos.setY(posY);

        return pos;
    }

    public abstract void serialize(Composer msg, boolean isNew);
    public abstract void serialize(Composer msg);

    public abstract ItemDefinition getDefinition();

    @Deprecated
    public abstract boolean handleInteraction(boolean state);

    public abstract boolean toggleInteract(boolean state);
    public abstract void sendUpdate();
    public abstract void saveData();

    public abstract String getExtraData();
    public abstract void setExtraData(String data);
}
