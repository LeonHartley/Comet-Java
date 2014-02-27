package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.items.interactions.InteractionAction;
import com.cometproject.server.game.items.interactions.InteractionQueueItem;
import com.cometproject.server.game.items.interactions.football.BallInteraction;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.RoomEntityType;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.game.wired.misc.WiredSquare;
import com.cometproject.server.game.wired.types.TriggerType;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManagement;
import com.cometproject.server.utilities.TimeSpan;
import javolution.util.FastList;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ItemProcessComponent implements CometTask {
    private Room room;

    private Logger log;
    private ScheduledFuture myFuture;
    private CometThreadManagement mgr;
    private boolean active = false;
    private int interval = Integer.parseInt(Comet.getServer().getConfig().get("comet.system.item_process.interval"));
    private int rollCounter = 0;

    private int flag = Integer.parseInt(Comet.getServer().getConfig().get("comet.system.item_process.flag"));

    public ItemProcessComponent(CometThreadManagement mgr, Room room) {
        this.mgr = mgr;
        this.room = room;

        this.myFuture = this.mgr.executePeriodic(this, interval, interval, TimeUnit.MILLISECONDS);

        log = Logger.getLogger("GenericRoomItem Process [" + room.getData().getName() + "]");
    }

    public void start() {
        if(this.myFuture != null && this.active) {
            stop();
        }

        this.active = true;
        this.myFuture = this.mgr.executePeriodic(this, interval, interval, TimeUnit.MILLISECONDS);

        log.debug("Processing started");
    }

    public void stop() {
        if(this.myFuture != null) {
            this.active = false;
            this.myFuture.cancel(false);

            log.debug("Processing stopped");
        }
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public void run() {
        try {
            if(!this.active) {
                return;
            }

            if(this.getRoom().getEntities().count() == 0) {
                this.stop();
            }

            boolean needsRoll = false;
            rollCounter++;

            if(rollCounter >= 2) {
                needsRoll = true;
                rollCounter = 0;
            }

            long timeStart = System.currentTimeMillis();

            for(FloorItem item : this.getRoom().getItems().getFloorItems()) {
                if (item.hasInteraction()) {
                    InteractionQueueItem interactItem = item.getNextInteraction();

                    if (interactItem != null) {
                        if (interactItem.getAction() == InteractionAction.ON_PLACED) {

                        } else if (interactItem.getAction() == InteractionAction.ON_PICKUP) {

                        } else if (interactItem.getAction() == InteractionAction.ON_USE) {
                            GameEngine.getItems().getInteractions().onInteract(interactItem.getUpdateState(), item, interactItem.getEntity());
                        } else if (interactItem.getAction() == InteractionAction.ON_WALK) {
                            GameEngine.getItems().getInteractions().onWalk(interactItem.getUpdateState() == 1, item, interactItem.getEntity());
                        } else if (interactItem.getAction() == InteractionAction.ON_TICK) {
                            GameEngine.getItems().getInteractions().onTick(item);
                        }else if (interactItem.getAction() == InteractionAction.ON_PRE_WALK) {
                            GameEngine.getItems().getInteractions().onPreWalk(item, interactItem.getEntity());
                        }
                    } else {
                        item.setNeedsUpdate(false);
                    }
                }

                if(needsRoll) {
                    if(item.isRolling()) {
                        doBallRoll(item);
                    }
                }

                if(GameEngine.getWired().isWiredTrigger(item)) {
                    if(!this.getRoom().getWired().isWiredSquare(item.getX(), item.getY())) {
                        this.getRoom().getWired().add(item.getX(), item.getY());
                    }
                }
            }


            for(WiredSquare wiredSquare : this.getRoom().getWired().getSquares()) {
                if(this.getRoom().getItems().getItemsOnSquare(wiredSquare.getX(), wiredSquare.getY()).size() < 1) {
                    this.getRoom().getWired().disposeSquare(wiredSquare);
                }
            }

            TimeSpan span = new TimeSpan(timeStart, System.currentTimeMillis());

            if(span.toMilliseconds() > flag) {
                log.warn("GenericRoomItem process took: " + span.toMilliseconds() + "ms to execute.");
            }
        } catch(Exception e) {
            log.error("Error while processing items", e);
        }
    }

    public void doBallRoll(FloorItem item) {
        Position3D nextPos = item.getRollingPositions().get(0);
        Position3D currentPos = new Position3D(item.getX(), item.getY(), item.getHeight());

        if(this.getRoom().getMapping().isValidStep(currentPos, nextPos, false)) {
            BallInteraction.roll(item, currentPos, nextPos, room);

            item.setX(nextPos.getX());
            item.setY(nextPos.getY());

            item.setNeedsUpdate(true);

            item.getRollingPositions().remove(0);
        } else {
            int length = item.getRollingPositions().size();
            List<Position3D> newPositions = new FastList<>();

            for(int i = 0; i < length; i++) {
                Position3D pos = item.getRollingPositions().get(i);

                newPositions.add(BallInteraction.calculatePosition(pos.getX(), pos.getY(), item.getRotation(), true));
            }

            item.setRollingPositions(newPositions);
        }
    }

    public void dispose() {
        if (this.myFuture != null) { this.myFuture.cancel(false); }
        this.myFuture = null;
    }

    public Room getRoom() {
        return this.room;
    }
}