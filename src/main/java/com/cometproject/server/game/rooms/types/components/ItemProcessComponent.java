package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManagement;
import org.apache.log4j.Logger;

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

        log = Logger.getLogger("GenericRoomItem Process [" + room.getData().getName() + "]");
    }

    public void start() {
        if (this.myFuture != null && this.active) {
            stop();
        }

        this.active = true;
        this.myFuture = this.mgr.executePeriodic(this, 0, interval, TimeUnit.MILLISECONDS);

        log.debug("Processing started");
    }

    public void stop() {
        if (this.myFuture != null) {
            this.active = false;
            this.myFuture.cancel(false);

            /*for (FloorItem item : this.getRoom().getItems().getFloorItems()) {
                if (item.hasInteraction()) {
                    item.getInteractionQueue().clear();
                }
            }*/

            log.debug("Processing stopped");
        }
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public void run() {
        this.getRoom().getItems().getFloorItems().parallelStream().forEach((item) -> {
            if (item.requiresTick()) {
                item.tick();
            }
        });
    }

    /*@Override
    public void run() {
        try {
            if (!this.active) {
                return;
            }

            if (this.getRoom().getEntities().playerCount() == 0) {
                this.stop();
                return;
            }

            boolean needsRoll = false;

            this.rollCounter++;

            if (this.rollCounter >= 2) {
                needsRoll = true;
                this.rollCounter = 0;
            }

            long timeStart = System.currentTimeMillis();

            for (FloorItem item : this.getRoom().getItems().getFloorItems()) {
                if (item.hasInteraction()) {
                    InteractionQueueItem interactItem = item.getNextInteraction();

                    if (interactItem != null) {
                        if (interactItem.getAction() == InteractionAction.ON_PLACED) {

                        } else if (interactItem.getAction() == InteractionAction.ON_PICKUP) {

                        } else if (interactItem.getAction() == InteractionAction.ON_USE) {
                            CometManager.getItems().getInteractions().onInteract(interactItem.getUpdateState(), item, interactItem.getEntity());
                        } else if (interactItem.getAction() == InteractionAction.ON_WALK) {
                            CometManager.getItems().getInteractions().onWalk(interactItem.getUpdateState() == 1, item, interactItem.getEntity());
                        } else if (interactItem.getAction() == InteractionAction.ON_TICK) {
                            CometManager.getItems().getInteractions().onTick(item);
                        } else if (interactItem.getAction() == InteractionAction.ON_PRE_WALK) {
                            CometManager.getItems().getInteractions().onPreWalk(item, interactItem.getEntity());
                        }
                    } else {
                        item.setNeedsUpdate(false);
                    }
                }

                if (item.getDefinition().getInteraction().equals("roller")) {
                    // make tick
                    boolean needsTick = false;

                    for (FloorItem rollerStack : room.getItems().getItemsOnSquare(item.getX(), item.getY())) {// && !item.hasInteraction()) {
                        if (!needsTick && !rollerStack.getDefinition().getInteraction().equals("roller")) {
                            needsTick = true;
                        }
                    }

                    if (needsTick) {
                        int speed = this.room.hasAttribute("setspeed") ? (int)this.room.getAttribute("setspeed") : 9;

                       item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, null, 0, speed));
                    }
                }

                if (needsRoll) {
                    if (item.isRolling()) {
                        doBallRoll(item);
                    }
                }

                if (CometManager.getWired().isWiredTrigger(item)) {
                    if (!this.getRoom().getWired().isWiredSquare(item.getX(), item.getY())) {
                        this.getRoom().getWired().add(item.getX(), item.getY());
                    }
                }
            }

            for (WallItem item : this.getRoom().getItems().getWallItems()) {
                if (item.hasInteraction()) {
                    InteractionQueueItem interactItem = item.getNextInteraction();

                    if (interactItem != null) {
                        if (interactItem.getAction() == InteractionAction.ON_PLACED) {

                        } else if (interactItem.getAction() == InteractionAction.ON_PICKUP) {

                        } else if (interactItem.getAction() == InteractionAction.ON_USE) {
                            CometManager.getItems().getInteractions().onInteract(interactItem.getUpdateState(), item, interactItem.getEntity());
                        } else if (interactItem.getAction() == InteractionAction.ON_TICK) {
                            CometManager.getItems().getInteractions().onTick(item);
                        }
                    }
                }
            }

            synchronized (this.getRoom().getWired().getSquares()) {
                for (WiredSquare wiredSquare : this.getRoom().getWired().getSquares()) {
                    if (this.getRoom().getItems().getItemsOnSquare(wiredSquare.getX(), wiredSquare.getY()).size() < 1) {
                        this.getRoom().getWired().disposeSquare(wiredSquare);
                    }
                }
            }

            TimeSpan span = new TimeSpan(timeStart, System.currentTimeMillis());

            if (span.toMilliseconds() > flag) {
                log.warn("GenericRoomItem process took: " + span.toMilliseconds() + "ms to execute.");
            }
        } catch (Exception e) {
            log.error("Error while processing items", e);
        }
    }

    public void doBallRoll(FloorItem item) {
        Position3D nextPos = item.getRollingPositions().get(0);
        Position3D currentPos = new Position3D(item.getX(), item.getY(), item.getHeight());

        currentPos.setZ(room.getModel().getSquareHeight()[currentPos.getX()][currentPos.getY()]);
        nextPos.setZ(room.getModel().getSquareHeight()[nextPos.getX()][nextPos.getY()]);

        if (this.getRoom().getMapping().isValidStep(currentPos, nextPos, false)) {
            BallInteraction.roll(item, currentPos, nextPos, room);

            item.setX(nextPos.getX());
            item.setY(nextPos.getY());
            item.setHeight((float) nextPos.getZ());

            item.setNeedsUpdate(true);
        }

        item.getRollingPositions().remove(0);
    }*/

    public void dispose() {
        if (this.myFuture != null) {
            this.myFuture.cancel(false);
        }
        this.myFuture = null;
    }

    public Room getRoom() {
        return this.room;
    }
}